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

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.yarn.thrift.hb.gen.NodeInfo;
import org.springframework.yarn.thrift.hb.gen.NodeType;

/**
 * Representation of a node participating in
 * a heartbeats. From a master point of view
 * this will identify the node.
 *
 * @author Janne Valkealahti
 * @author Arun Suresh
 *
 */
public class HeartbeatNode {

	private final String nodeId;
	private final NodeType nodeType;

	/**
	 * Instantiates a new heartbeat node.
	 *
	 * @param nodeId the node id
	 * @param nodeType the node type
	 */
	public HeartbeatNode(String nodeId, NodeType nodeType) {
		this.nodeId = nodeId;
		this.nodeType = nodeType;
	}

	/**
	 * Gets the node id.
	 *
	 * @return the node id
	 */
	public String getId() {
		return nodeId;
	}

	/**
	 * Gets the node type.
	 *
	 * @return the node type
	 */
	public NodeType getType() {
		return nodeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((nodeType == null) ? 0 : nodeType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeartbeatNode other = (HeartbeatNode) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (nodeType != other.nodeType)
			return false;
		return true;
	}

	/**
	 * Encapsulates node state by keeping timestamp,
	 * host and port.
	 */
	public static class NodeState {
		public volatile AtomicLong timeStamp = new AtomicLong(0);
		public volatile NodeInfo nodeInfo;
		public volatile String host;
		public volatile int port;

		@Override
		public String toString() {
			return "NodeState [timeStamp=" + timeStamp + ", nodeInfo="
					+ nodeInfo + ", host=" + host + ", port=" + port + "]";
		}
	}

}