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

import org.springframework.yarn.thrift.hb.HeartbeatNode.NodeState;

/**
 * Interface for client having an interest to be
 * notified of heartbeat node states.
 *
 * @author Janne Valkealahti
 * @author Arun Suresh
 *
 */
public interface HeartbeatMasterClient {

	/**
	 * Called first time when node has up state
	 *
	 * @param node the node
	 * @param state the state
	 */
	void nodeUp(HeartbeatNode node, NodeState state);

	/**
	 * Called when node gets into warn state.
	 *
	 * @param node the node
	 * @param state the state
	 */
	void nodeWarn(HeartbeatNode node, NodeState state);

	/**
	 * Called when node gets into dead state.
	 *
	 * @param node the node
	 * @param state the state
	 */
	void nodeDead(HeartbeatNode node, NodeState state);

}