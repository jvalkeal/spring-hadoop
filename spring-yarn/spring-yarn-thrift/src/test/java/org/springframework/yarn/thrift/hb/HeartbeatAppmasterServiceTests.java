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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.am.AppmasterServiceClient;
import org.springframework.yarn.thrift.hb.HeartbeatNode.NodeState;
import org.springframework.yarn.thrift.hb.gen.NodeInfo;

/**
 * Tests for heartbeat system
 *
 * @author Janne Valkealahti
 *
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HeartbeatAppmasterServiceTests {

	private final static Log log = LogFactory.getLog(HeartbeatAppmasterServiceTests.class);

	@Autowired
	ApplicationContext ctx;

	@Autowired
	AppmasterService appmasterService;

	@Autowired
	AppmasterServiceClient appmasterServiceClient;


	@Test
	public void testBaseLogic() throws Exception {
		assertNotNull(appmasterService);
		assertNotNull(appmasterServiceClient);

        assertTrue(appmasterService.getPort() > 0);

        StubHeartbeatMasterClient stubListener = new StubHeartbeatMasterClient();
        ((HeartbeatAppmasterService)appmasterService).addHeartbeatMasterClientListener(stubListener);

        ((HeartbeatAppmasterServiceClient)appmasterServiceClient).setNodeInfo(new NodeInfo());

        Thread.sleep(5000);
        assertTrue(stubListener.up);
        assertFalse(stubListener.warn);
        assertFalse(stubListener.dead);

        ((HeartbeatAppmasterServiceClient)appmasterServiceClient).stop();
        Thread.sleep(15000);
        assertTrue(stubListener.up);
        assertTrue(stubListener.warn);
        assertTrue(stubListener.dead);

	}

	private class StubHeartbeatMasterClient implements HeartbeatMasterClient {

		public boolean up;
		public boolean warn;
		public boolean dead;

		@Override
		public void nodeWarn(HeartbeatNode node, NodeState state) {
			log.info("nodeWarn");
			warn = true;
		}
		@Override
		public void nodeUp(HeartbeatNode node, NodeState state) {
			log.info("nodeUp");
			up = true;
		}
		@Override
		public void nodeDead(HeartbeatNode node, NodeState state) {
			log.info("nodeDead");
			dead = true;
		}

	}

}
