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
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.util.Assert;
import org.springframework.yarn.YarnSystemException;
import org.springframework.yarn.thrift.ThriftAppmasterService;
import org.springframework.yarn.thrift.ThriftCallback;
import org.springframework.yarn.thrift.ThriftTemplate;
import org.springframework.yarn.thrift.hb.HeartbeatNode.NodeState;
import org.springframework.yarn.thrift.hb.gen.HeartbeatCommandEndPoint;
import org.springframework.yarn.thrift.hb.gen.HeartbeatCommandMessage;
import org.springframework.yarn.thrift.hb.gen.HeartbeatEndPoint;
import org.springframework.yarn.thrift.hb.gen.HeartbeatMessage;
import org.springframework.yarn.thrift.hb.gen.NodeType;

/**
 * General service providing heartbeat system for nodes.
 *
 * @author Janne Valkealahti
 * @author Arun Suresh
 *
 */
public class HeartbeatAppmasterService extends ThriftAppmasterService implements HeartbeatEndPoint.Iface {

	private final static Log log = LogFactory.getLog(HeartbeatAppmasterService.class);

	/** Default node warn time */
	private final static int DEFAULT_WARN_TIME = 30000;

	/** Default node dead time */
	private final static int DEFAULT_DEAD_TIME = 60000;

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

	/** Shared session id */
	private String sessionId;

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
		return new HeartbeatEndPoint.Processor<HeartbeatAppmasterService>(this);
	}

	@Override
	public boolean acceptHeartbeat(String sessionId, HeartbeatMessage heartbeatMessage) throws TException {
		NodeType nodeType = heartbeatMessage.getNodeType();
		String nodeId = heartbeatMessage.getNodeId();

		if (this.sessionId != null && !this.sessionId.equals(sessionId)) {
			log.warn("Not valid session id for node nodeId=" + nodeId + " sessionId=" + sessionId);
			return false;
		}

		if (log.isDebugEnabled()) {
			log.debug("Received Heartbeat from node nodeId=" + nodeId + " sessionId=" + sessionId);
		}

		ConcurrentHashMap<HeartbeatNode, NodeState> mTemp = new ConcurrentHashMap<HeartbeatNode, NodeState>();
		ConcurrentHashMap<HeartbeatNode, NodeState> m = nodeRegistry.putIfAbsent(nodeType, mTemp);
		if (m == null) {
			m = mTemp;
		}
		long now = System.currentTimeMillis();
		NodeState nsTemp = new NodeState();
		HeartbeatNode hKey = new HeartbeatNode(nodeId, nodeType);
		NodeState ns = m.putIfAbsent(hKey, nsTemp);
		boolean isFirst = false;
		if (ns == null) {
			ns = nsTemp;
			isFirst = true;
		}
		ns.timeStamp.set(now);
		ns.host = heartbeatMessage.getHost();
		ns.port = heartbeatMessage.getCommandPort();
		ns.nodeInfo = heartbeatMessage.getNodeInfo();
		// notify node up
		if (isFirst) {
			stateListener.nodeUp(hKey, ns);
		}

		return true;
	}

	public void sendCommand(HeartbeatCommandMessage heartbeatCommandMessage) {
		ConcurrentHashMap<HeartbeatNode, NodeState> containerMap = nodeRegistry.get(NodeType.CONTAINER);
		for (Entry<HeartbeatNode, NodeState> entry : containerMap.entrySet()) {
			try {
				sendCommandViaThrift(entry.getValue().host, entry.getValue().port, heartbeatCommandMessage);
			} catch (Exception e) {
				log.error("Error sending command to client", e);
			}
		}
	}

	private void sendCommandViaThrift(String host, int port, final HeartbeatCommandMessage heartbeatCommandMessage) throws Exception {

		TTransport transport = new TFramedTransport(new TSocket(host, port, 2000));
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);

		ThriftTemplate<HeartbeatCommandEndPoint.Client> template = new ThriftTemplate<HeartbeatCommandEndPoint.Client>(
				HeartbeatCommandEndPoint.class, protocol);
		template.afterPropertiesSet();

		template.executeClient(new ThriftCallback<Boolean, HeartbeatCommandEndPoint.Client>() {
			@Override
			public Boolean doInThrift(HeartbeatCommandEndPoint.Client proxy)
					throws TException {
				return proxy.command(sessionId, heartbeatCommandMessage);
			}
		});

	}

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
	 * @param nodeType the node type
	 * @return the node state
	 */
	public NodeState getNodeState(String nodeId, NodeType nodeType) {
		ConcurrentHashMap<HeartbeatNode, NodeState> map = nodeRegistry.get(nodeType);
		if (map != null) {
			return map.get(new HeartbeatNode(nodeId, nodeType));
		}
		return null;
	}

	/**
	 * Sets the session id. Session id is a shared secret to
	 * guard this service against thrift clients who are not
	 * allowed to talk to the service.
	 *
	 * @param sessionId the new session id
	 */
	public void setSessionId(String sessionId) {
		log.info("Setting sessionId=" + sessionId);
		this.sessionId = sessionId;
	}

	/**
	 * Handle task work.
	 *
	 * @return true, if successful
	 */
	private boolean handleTaskWork() {
		long now = System.currentTimeMillis();
		List<HeartbeatNode> nodesToDelete = new ArrayList<HeartbeatNode>();
		List<HeartbeatNode> nodesToWarn = new ArrayList<HeartbeatNode>();

		for (Entry<NodeType, ConcurrentHashMap<HeartbeatNode, NodeState>> entry1 : nodeRegistry.entrySet()) {
			NodeType nodeType = entry1.getKey();
			ConcurrentHashMap<HeartbeatNode, NodeState> map = entry1.getValue();

			for (Entry<HeartbeatNode, NodeState> entry2 : map.entrySet()) {
				HeartbeatNode node = entry2.getKey();
				long diff = now - entry2.getValue().timeStamp.get();
				if (log.isDebugEnabled()) {
					log.debug("Found Node [" + nodeType + ", "	+ node.getId() + "]");
				}
				if (diff > warnTime) {
					if (log.isDebugEnabled()) {
						log.debug("Node hasnt sent Heartbeats for a while [" + nodeType + ", " + node.getId() + "]");
					}
					if (diff > deadTime) {
						nodesToDelete.add(node);
						if (log.isDebugEnabled()) {
							log.debug("Node deemed dead [" + nodeType + ", " + node.getId() + "]");
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

	/**
	 * Creates the poller for heartbeat system.
	 *
	 * @return the runnable
	 */
	private Runnable createPoller() {
		Callable<Boolean> pollingTask = new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return handleTaskWork();
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

//	private static class NodeRegistry {
//
//		private final ConcurrentHashMap<NodeType, ConcurrentHashMap<HeartbeatNode, NodeState>> registry =
//				new ConcurrentHashMap<NodeType, ConcurrentHashMap<HeartbeatNode, NodeState>>();
//
//		public NodeRegistry(NodeType... nodeTypes) {
//			for (NodeType nodeType : nodeTypes) {
//				registry.put(nodeType, new ConcurrentHashMap<HeartbeatNode, NodeState>());
//			}
//		}
//
//		public void put(NodeType nodeType) {
////			ConcurrentHashMap<HeartbeatNode, NodeState> map = registry.get(nodeType);
//		}
//
//	}

}
