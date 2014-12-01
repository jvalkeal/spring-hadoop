/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.yarn.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.exceptions.YarnRuntimeException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.OrderComparator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.yarn.listener.ContainerStateListener.ContainerState;

/**
 * Default implementation of a {@link YarnContainer}.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultYarnContainer extends AbstractYarnContainer implements BeanFactoryAware {

	private final static Log log = LogFactory.getLog(DefaultYarnContainer.class);

	private final ResultsProcessor resultsProcessor = new ResultsProcessor(this);

	private final AtomicBoolean endNotified = new AtomicBoolean();

	@Override
	protected void runInternal() {
		try {
			handleResults(getContainerHandlerResults(getContainerHandlers()));
		} catch (Exception e) {
			resultsProcessor.runtimeException = e;
			resultsProcessor.mayNotifyEndState();
		}
	}

	@Override
	protected void doStop() {
		log.info("Stopping DefaultYarnContainer and cancelling Futures");
		// need to cancel pending futures
		resultsProcessor.cancelIfFutures();
	}

	@Override
	public boolean isWaitCompleteState() {
		// we need to tell boot ContainerLauncherRunner that we're
		// about to notify state via events so it should wait
		return true;
	}

	private List<ContainerHandler> getContainerHandlers() {
		BeanFactory bf = getBeanFactory();
		Assert.state(bf instanceof ListableBeanFactory, "Bean factory must be instance of ListableBeanFactory");
		Map<String, ContainerHandler> handlers = ((ListableBeanFactory)bf).getBeansOfType(ContainerHandler.class);
		List<ContainerHandler> handlersList = new ArrayList<ContainerHandler>(handlers.values());
		OrderComparator comparator = new OrderComparator();
		Collections.sort(handlersList, comparator);
		return handlersList;
	}

	private boolean isEmptyValues(List<Object> results) {
		for (Object o : results) {
			if (o != null) {
				if (o instanceof String) {
					if (StringUtils.hasText((String)o)) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	private void notifyEndState(List<Object> results, Exception runtimeException) {
		if (endNotified.getAndSet(true)) {
			log.warn("We already notified end state, discarding this");
			return;
		}
		log.info("Container state based on method results=[" + StringUtils.arrayToCommaDelimitedString(results.toArray())
				+ "] runtimeException=[" + runtimeException + "]");
		if (runtimeException != null) {
			notifyContainerState(ContainerState.FAILED, runtimeException);
		} else if (!isEmptyValues(results)) {
			if (results.size() == 1) {
				notifyContainerState(ContainerState.COMPLETED, results.get(0));
			} else {
				notifyContainerState(ContainerState.COMPLETED, results);
			}
		} else {
			notifyCompleted();
		}
	}

	private void handleResults(List<Object> resultsx) {
		resultsProcessor.addResults(resultsx);
		if (!resultsProcessor.hasActiveListenables()) {
			notifyEndState(resultsProcessor.getResults(), resultsProcessor.runtimeException);
		}
	}

	private List<Object> getContainerHandlerResults(List<ContainerHandler> containerHandlers) {
		List<Object> results = new ArrayList<Object>();
		for (ContainerHandler handler : containerHandlers) {
			results.add(handler.handle(this));
		}
		return results;
	}

	/**
	 * Helper class processing results and installing callbacks
	 * if result is an instance of {@link ListenableFuture}.
	 */
	private static class ResultsProcessor {

		final DefaultYarnContainer container;
		List<Result> wrappedResults = new ArrayList<Result>();
		AtomicInteger activeListenables = new AtomicInteger();
		Exception runtimeException = null;

		public ResultsProcessor(DefaultYarnContainer container) {
			this.container = container;
		}

		boolean hasActiveListenables() {
			return activeListenables.get() > 0;
		}

		void mayNotifyEndState() {
			if (!hasActiveListenables()) {
				container.notifyEndState(getResults(), runtimeException);
			}
		}

		void addResults(List<Object> results) {
			// first, wrap results without registering callback
			for (Object result : results) {
				wrappedResults.add(new Result(result));
				if (result instanceof ListenableFuture<?>) {
					activeListenables.incrementAndGet();
				}
			}

			// second, register callbacks
			for (final Result wrappedResult : wrappedResults) {
				if (wrappedResult.result instanceof ListenableFuture<?>) {
					((ListenableFuture<?>) wrappedResult.result).addCallback(new ListenableFutureCallback<Object>() {

						@Override
						public void onSuccess(Object result) {
							if (log.isDebugEnabled()) {
								log.info("onSuccess for " + wrappedResult + " with result=[" + result + "]");
							}
							wrappedResult.setResult(result);
							activeListenables.decrementAndGet();
							mayNotifyEndState();
						}

						@Override
						public void onFailure(Throwable t) {
							if (log.isDebugEnabled()) {
								log.info("onFailure for " + wrappedResult + " with throwable=[" + t + "]");
							}
							runtimeException = new YarnRuntimeException(t);
							activeListenables.decrementAndGet();
							mayNotifyEndState();
						}

					});

				}
			}
		}

		void cancelIfFutures() {
			for (final Result wrappedResult : wrappedResults) {
				try {
					log.info("Cancelling " + wrappedResult);
					wrappedResult.cancelIfFuture();
				} catch (Exception e) {
					log.error("error in cancel", e);
				}
			}
		}

		List<Object> getResults() {
			List<Object> res = new ArrayList<Object>();
			for (Result r : wrappedResults) {
				try {
					res.add(r.getResult());
				} catch (Exception e) {
					log.debug("Future get() resulted error", e);
				}
			}
			return res;
		}
	}

	/**
	 * Wrapped for result object to make it easier to handle
	 * result as a {@link Future}.
	 */
	private static class Result {

		Object result;

		Result(Object result) {
			this.result = result;
		}

		/**
		 * Request a cancel if result is a {@link Future}.
		 */
		void cancelIfFuture() {
			if (result instanceof Future<?>) {
				((Future<?>)result).cancel(true);
			}
		}

		/**
		 * Gets the result. If result is a future this
		 * method delegates to {@link Future#get()}.
		 *
		 * @return the result
		 */
		Object getResult() {
			if (result instanceof Future<?>) {
				Future<?> f = (Future<?>)result;
				try {
					return f.get();
				} catch (InterruptedException e) {
					f.cancel(true);
				} catch (ExecutionException e) {
					f.cancel(true);
				}
				return null;
			} else {
				return result;
			}
		}

		/**
		 * Sets the result.
		 *
		 * @param result the new result
		 */
		void setResult(Object result) {
			this.result = result;
		}

	}

}
