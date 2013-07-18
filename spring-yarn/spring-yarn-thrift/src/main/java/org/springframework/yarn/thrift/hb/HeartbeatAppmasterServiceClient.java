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
package org.springframework.yarn.thrift.hb;

import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.util.Assert;
import org.springframework.yarn.YarnSystemException;
import org.springframework.yarn.thrift.ThriftAppmasterServiceClient;
import org.springframework.yarn.thrift.ThriftCallback;
import org.springframework.yarn.thrift.ThriftTemplate;
import org.springframework.yarn.thrift.hb.gen.HeartbeatMsg;
import org.springframework.yarn.thrift.hb.gen.NodeInfo;
import org.springframework.yarn.thrift.hb.gen.NodeType;
import org.springframework.yarn.thrift.hb.gen.THeartbeatCommandEndPoint;
import org.springframework.yarn.thrift.hb.gen.THeartbeatEndPoint;
import org.springframework.yarn.thrift.hb.gen.THeartbeatEndPoint.Client;

/**
 * Client side heartbeat service accessor.
 *
 * @author Janne Valkealahti
 * @author Arun Suresh
 *
 */
public class HeartbeatAppmasterServiceClient extends ThriftAppmasterServiceClient
		implements THeartbeatCommandEndPoint.Iface {

	private final static Log log = LogFactory.getLog(HeartbeatAppmasterServiceClient.class);

	/** Trigger for scheduled task */
	private volatile Trigger trigger = new PeriodicTrigger(2000);

	/** Task runnable */
	private volatile Runnable poller;

	/** Current running task if any */
	private volatile ScheduledFuture<?> runningTask;

	/** Node info for this client */
	private volatile NodeInfo nodeInfo = null;

	/** Node id reported by this client */
	private volatile String nodeId;

	/**
	 * Sets the new node info.
	 *
	 * @param nodeInfo the new node info
	 */
	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	/**
	 * Sets the new node id.
	 *
	 * @param nodeId the new node id
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Sets the trigger.
	 *
	 * @param trigger the new trigger
	 */
	public void setTrigger(Trigger trigger) {
		Assert.notNull(trigger, "Trigger must not be null");
		this.trigger = trigger;
	}

	/**
	 * Do accept heartbeat.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean doAcceptHeartbeat() throws Exception {
		if (nodeInfo == null) {
			if (log.isDebugEnabled()) {
				log.debug("nodeInfo not set, delaying heartbeat");
			}
			return false;
		}

		if (log.isDebugEnabled()) {
			log.debug("Executing heartbeat");
		}

		final HeartbeatMsg hMsg = new HeartbeatMsg();
		hMsg.setNodeId(nodeId);
		hMsg.setNodeType(NodeType.CONTAINER);
		hMsg.setNodeInfo(nodeInfo);
		hMsg.setHost(InetAddress.getLocalHost().getHostName());
		hMsg.setCommandPort(getThriftServerPort());

		ThriftTemplate<THeartbeatEndPoint.Client> template =
				getClientThriftTemplate(THeartbeatEndPoint.class, THeartbeatEndPoint.Client.class);

		Boolean accepted = template.executeClient(new ThriftCallback<Boolean, THeartbeatEndPoint.Client>() {
			@Override
			public Boolean doInThrift(Client proxy) throws TException{
				return proxy.acceptHeartbeat(hMsg);
			}
		});

		if (log.isDebugEnabled()) {
			log.debug("Heartbeat accepted=" + accepted);
		}
		return accepted;
	}

	@Override
	protected void onInit() throws Exception {
		super.onInit();
		Assert.notNull(trigger, "Trigger is required");
		try {
			this.poller = this.createPoller();
		} catch (Exception e) {
			throw new YarnSystemException("Failed to create Poller", e);
		}
	}

	@Override
	protected void doStart() {
		super.doStart();
		Assert.state(getTaskScheduler() != null, "unable to start heartbeat, no taskScheduler available");
		runningTask = getTaskScheduler().schedule(this.poller, this.trigger);
	}

	@Override
	protected void doStop() {
		super.doStop();
		if (this.runningTask != null) {
			this.runningTask.cancel(true);
		}
		this.runningTask = null;
	}

	@Override
	protected TProcessor getProcessor() {
		return new THeartbeatCommandEndPoint.Processor<HeartbeatAppmasterServiceClient>(this);
	}

	@Override
	public boolean changeEndPoint(String host, int port) throws TException {
		return false;
	}

	@Override
	public boolean killSelf() throws TException {
		return false;
	}

	/**
	 * Creates the poller for heartbeat system.
	 *
	 * @return the runnable
	 */
	private Runnable createPoller() {
		Callable<Boolean> pollingTask = new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return doAcceptHeartbeat();
			}
		};
		return new Poller(pollingTask);
	}

	/**
	 * Internal helper class for poller.
	 */
	private class Poller implements Runnable {

		private final Callable<Boolean> pollingTask;

		public Poller(Callable<Boolean> pollingTask) {
			this.pollingTask = pollingTask;
		}

		public void run() {
			getTaskExecutor().execute(new Runnable() {
				public void run() {
					try {
						pollingTask.call();
					} catch (Exception e) {
						throw new RuntimeException("Error executing polling task", e);
					}
				}
			});
		}
	}

}
