/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.yarn.thrift.support;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.Assert;
import org.springframework.yarn.support.LifecycleObjectSupport;
import org.springframework.yarn.thrift.ThriftServerFactoryBean;
import org.springframework.yarn.thrift.ThriftServerFactoryBean.ServerType;

/**
 * Base support class for service components
 * sharing some common functionality.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class ThriftObjectSupport extends LifecycleObjectSupport {

	private final static Log log = LogFactory.getLog(ThriftObjectSupport.class);

	/** Default lower range for free port scan */
	private final static int DEFAULT_MIN_FREE_PORT = 8200;

	/** Default upper range for free port scan */
	private final static int DEFAULT_MAX_FREE_PORT = 8300;

	/** Lower range for free port scan */
	private int minFreeServerPort = DEFAULT_MIN_FREE_PORT;

	/** Upper range for free port scan */
	private int maxFreeServerPort = DEFAULT_MAX_FREE_PORT;

	/** Either explicitly used port or set during the scan */
	private volatile int thriftServerPort = -1;

	/** Flag if thrift transport is started */
	private volatile boolean thriftServerStarted;

	/** Flag if thrift server is started automatically */
	private volatile boolean thriftServerAutostart;

	/** Lifecycle lock for thrift server */
	private volatile Object lock = new Object();

	@Override
	protected void doStart() {
		super.doStart();
		if (thriftServerAutostart) {
			try {
				startThriftServer();
			} catch (Exception e) {
				log.error("Failed to start thrift server", e);
			}
		}
	}

	/**
	 * Gets the thrift server port.
	 *
	 * @return the thrift server port
	 */
	public int getThriftServerPort() {
		return thriftServerPort;
	}

	/**
	 * Checks if is thrift server is started.
	 *
	 * @return true, if is thrift server is started
	 */
	public boolean isThriftServerStarted() {
		return thriftServerStarted;
	}

	/**
	 * Sets the thrift server port.
	 *
	 * @param thriftServerPort the new thrift server port
	 */
	public void setThriftServerPort(int thriftServerPort) {
		this.thriftServerPort = thriftServerPort;
	}

	/**
	 * Sets the port range.
	 *
	 * @param minPort the min port
	 * @param maxPort the max port
	 */
	public void setPortRange(int minPort, int maxPort) {
		Assert.isTrue(minPort < maxPort, "minPort must be lower than maxPort");
		minFreeServerPort = minPort;
		maxFreeServerPort = maxPort;
	}

	/**
	 * Checks if thrift server is started automatically.
	 *
	 * @return true, if is thrift server autostart is enabled
	 */
	public boolean isThriftServerAutostart() {
		return thriftServerAutostart;
	}

	/**
	 * Sets the thrift server autostart.
	 *
	 * @param thriftServerAutostart the new thrift server autostart
	 */
	public void setThriftServerAutostart(boolean thriftServerAutostart) {
		this.thriftServerAutostart = thriftServerAutostart;
	}

	/**
	 * Gets the processor.
	 *
	 * @return the processor
	 */
	protected abstract TProcessor getProcessor();

	/**
	 * Gets the thrift server type. User can override
	 * default {@link org.springframework.yarn.thrift.ThriftServerFactoryBean.ServerType.NONBLOCK_HSHA}
	 * returned from this method.
	 *
	 * @return the server type
	 */
	protected ServerType getServerType() {
		return ServerType.NONBLOCK_HSHA;
	}

	/**
	 * Creates the server transport.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void startThriftServer() throws Exception {
		synchronized (lock) {
			if (isThriftServerStarted()) {
				log.info("Thrift server allready started, bailing out.");
				return;
			}
			TaskExecutor taskExecutor = getTaskExecutor();
			Assert.notNull(taskExecutor, "Task Executor must be available");

			final TServer server = createThriftServer();

			// serve is blocking so pass it to executor
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					server.serve();
				}
			});
		}
	}

	/**
	 * Creates the thrift server.
	 *
	 * @return the thrift server
	 * @throws Exception the exception
	 */
	protected TServer createThriftServer() throws Exception {
		ThriftServerFactoryBean fb = new ThriftServerFactoryBean();
		fb.setProcessor(getProcessor());
		fb.setServerType(getServerType());
		fb.setThriftServerPort(thriftServerPort);
		fb.setMinFreeServerPort(minFreeServerPort);
		fb.setMaxFreeServerPort(maxFreeServerPort);
		fb.afterPropertiesSet();
		thriftServerStarted = true;
		thriftServerPort = fb.getThriftServerPort();
		return fb.getObject();
	}

}
