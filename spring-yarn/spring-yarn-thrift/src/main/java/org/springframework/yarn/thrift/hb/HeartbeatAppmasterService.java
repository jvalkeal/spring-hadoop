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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.util.Assert;
import org.springframework.yarn.YarnSystemException;
import org.springframework.yarn.thrift.ThriftAppmasterService;
import org.springframework.yarn.thrift.hb.HeartbeatNode.NodeState;
import org.springframework.yarn.thrift.hb.gen.HeartbeatMsg;
import org.springframework.yarn.thrift.hb.gen.NodeType;
import org.springframework.yarn.thrift.hb.gen.THeartbeatEndPoint;

/**
 * General service providing heartbeat system for nodes.
 *
 * @author Janne Valkealahti
 * @author Arun Suresh
 *
 */
public class HeartbeatAppmasterService extends ThriftAppmasterService<HeartbeatMsg> implements THeartbeatEndPoint.Iface {

	private final static Log log = LogFactory.getLog(HeartbeatAppmasterService.class);

	/** Default node warn time */
	private final static int DEFAULT_WARN_TIME = 30000;

	/** Default node dead time */
	private final static int DEFAULT_DEAD_TIME = 30000;

	/** Map of known nodes */
	private final ConcurrentHashMap<NodeType, ConcurrentHashMap<HeartbeatNode, NodeState>> nodeRegistry =
			new ConcurrentHashMap<NodeType, ConcurrentHashMap<HeartbeatNode, NodeState>>();

	/** Time to consider node warning */
	private int warnTime = DEFAULT_WARN_TIME;

	/** Time to consider node dead */
	private int deadTime = DEFAULT_DEAD_TIME;

	/** Trigger for scheduled task */
	private volatile Trigger trigger = new PeriodicTrigger(2000);

	/** Task runnable */
	private volatile Runnable poller;

	/** Current running task if any */
	private volatile ScheduledFuture<?> runningTask;

	/** Listener handling state events */
	private CompositeHeartbeatMasterClientListener stateListener = new CompositeHeartbeatMasterClientListener();

	/**
	 * Gets the warn time.
	 *
	 * @return the warn time
	 */
	public int getWarnTime() {
		return warnTime;
	}

	/**
	 * Sets the warn time.
	 *
	 * @param warnTime the new warn time
	 */
	public void setWarnTime(int warnTime) {
		this.warnTime = warnTime;
	}

	/**
	 * Gets the dead time.
	 *
	 * @return the dead time
	 */
	public int getDeadTime() {
		return deadTime;
	}

	/**
	 * Sets the dead time.
	 *
	 * @param deadTime the new dead time
	 */
	public void setDeadTime(int deadTime) {
		this.deadTime = deadTime;
	}


	/**
	 * Adds the appmaster state listener.
	 *
	 * @param listener the listener
	 */
	public void addHeartbeatMasterClientListener(HeartbeatMasterClient listener) {
		stateListener.register(listener);
	}

	/**
	 * Gets the node state.
	 *
	 * @param nodeId the node id
	 * @param nType the n type
	 * @return the node state
	 */
	public NodeState getNodeState(String nodeId, NodeType nType) {
		ConcurrentHashMap<HeartbeatNode, NodeState> m1 = nodeRegistry.get(nType);
		if (m1 != null) {
			return m1.get(new HeartbeatNode(nodeId, nType));
		}
		return null;
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

	private boolean doStuff() {
		long now = System.currentTimeMillis();
		List<HeartbeatNode> nodesToDelete = new ArrayList<HeartbeatNode>();
		List<HeartbeatNode> nodesToWarn = new ArrayList<HeartbeatNode>();

		for (Entry<NodeType, ConcurrentHashMap<HeartbeatNode, NodeState>> e : nodeRegistry.entrySet()) {
			NodeType nType = e.getKey();
			ConcurrentHashMap<HeartbeatNode, NodeState> m = e.getValue();

			for (Entry<HeartbeatNode, NodeState> e2 : m.entrySet()) {
				HeartbeatNode node = e2.getKey();
				long diff = now - e2.getValue().timeStamp.get();
				if (log.isDebugEnabled()) {
					log.debug("Found Node [" + nType + ", "	+ node.getId() + "]");
				}
				if (diff > warnTime) {
					if (log.isDebugEnabled()) {
						log.debug("Node hasnt sent Heartbeats for a while [" + nType + ", " + node.getId() + "]");
					}
					if (diff > deadTime) {
						nodesToDelete.add(node);
						if (log.isDebugEnabled()) {
							log.debug("Node deemed dead [" + nType + ", " + node.getId() + "]");
						}
					} else {
						nodesToWarn.add(node);
					}
				}
			}
		}

		for (HeartbeatNode node : nodesToDelete) {
			NodeState nodeState = nodeRegistry.get(node.getType()).get(node);
			stateListener.nodeDead(node, nodeState);
			nodeRegistry.get(node.getType()).remove(node);
		}

		for (HeartbeatNode node : nodesToWarn) {
			NodeState nodeState = nodeRegistry.get(node.getType()).get(node);
			stateListener.nodeWarn(node, nodeState);
		}

		return true;
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
		return new THeartbeatEndPoint.Processor<HeartbeatAppmasterService>(this);
	}

	@Override
	public boolean acceptHeartbeat(HeartbeatMsg hbMsg) throws TException {
		NodeType nodeType = hbMsg.getNodeType();
		String nodeId = hbMsg.getNodeId();

		if (log.isDebugEnabled()) {
			log.debug("Received Heartbeat from node [" + nodeType + ", " + nodeId + "]");
		}

		ConcurrentHashMap<HeartbeatNode, NodeState> mTemp = new ConcurrentHashMap<HeartbeatNode, NodeState>();
		ConcurrentHashMap<HeartbeatNode, NodeState> m = nodeRegistry.putIfAbsent(nodeType, mTemp);
		if (m == null) {
			m = mTemp;
		}

		long cTime = System.currentTimeMillis();
		NodeState nsTemp = new NodeState();
		HeartbeatNode hKey = new HeartbeatNode(nodeId, nodeType);
		NodeState ns = m.putIfAbsent(hKey, nsTemp);

		// check if this is a first heartbeat
		boolean isFirst = false;
		if (ns == null) {
			ns = nsTemp;
			isFirst = true;
		}
		ns.timeStamp.set(cTime);
		ns.host = hbMsg.getHost();
		ns.port = hbMsg.getCommandPort();
		ns.nodeInfo = hbMsg.getNodeInfo();

		// notify node up
		if (isFirst) {
			stateListener.nodeUp(hKey, ns);
		}
		return true;
	}

	/**
	 * Creates the poller for heartbeat system.
	 *
	 * @return the runnable
	 */
	private Runnable createPoller() {
		Callable<Boolean> pollingTask = new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return doStuff();
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
