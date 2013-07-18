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

import java.util.Iterator;

import org.springframework.yarn.listener.AbstractCompositeListener;
import org.springframework.yarn.thrift.hb.HeartbeatNode.NodeState;

/**
 * Composite listener for handling heartbeat node state events.
 *
 * @author Janne Valkealahti
 *
 */
public class CompositeHeartbeatMasterClientListener extends AbstractCompositeListener<HeartbeatMasterClient>
		implements HeartbeatMasterClient {

	@Override
	public void nodeUp(HeartbeatNode node, NodeState state) {
		for (Iterator<HeartbeatMasterClient> iterator = getListeners().reverse(); iterator.hasNext();) {
			HeartbeatMasterClient listener = iterator.next();
			listener.nodeUp(node, state);
		}
	}

	@Override
	public void nodeWarn(HeartbeatNode node, NodeState state) {
		for (Iterator<HeartbeatMasterClient> iterator = getListeners().reverse(); iterator.hasNext();) {
			HeartbeatMasterClient listener = iterator.next();
			listener.nodeWarn(node, state);
		}
	}

	@Override
	public void nodeDead(HeartbeatNode node, NodeState state) {
		for (Iterator<HeartbeatMasterClient> iterator = getListeners().reverse(); iterator.hasNext();) {
			HeartbeatMasterClient listener = iterator.next();
			listener.nodeDead(node, state);
		}
	}

}
