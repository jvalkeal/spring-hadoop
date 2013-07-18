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
 * Adapter implementation of {@link HeartbeatMasterClient} providing
 * stub methods for the interface. This is useful is one part of the
 * interface needs to be implemented or generally if it is not
 * appropriate to expose whole interface as public methods.
 *
 * @author Janne Valkealahti
 *
 */
public class HeartbeatMasterClientAdapter implements HeartbeatMasterClient {

	@Override
	public void nodeUp(HeartbeatNode node, NodeState state) {
	}

	@Override
	public void nodeWarn(HeartbeatNode node, NodeState state) {
	}

	@Override
	public void nodeDead(HeartbeatNode node, NodeState state) {
	}

}
