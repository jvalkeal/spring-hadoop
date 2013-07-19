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
package org.springframework.yarn.thrift;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;
import org.springframework.yarn.thrift.ThriftServerFactoryBean.ServerType;
import org.springframework.yarn.thrift.gen.TestService;
import org.springframework.yarn.thrift.gen.TestService.Client;
import org.springframework.yarn.thrift.gen.TestService.Processor;

/**
 * Tests for {@link ThriftServerFactoryBean} and thrift
 * servers returned from it.
 *
 * @author Janne Valkealahti
 *
 */
public class ThriftServerFactoryBeanTests {

	@Test
	public void testHsHaServer() throws Exception {
		ThriftServerFactoryBean fb = setupFactoryForServer(ServerType.NONBLOCK_HSHA);
		int thriftServerPort = fb.getThriftServerPort();
		final TServer server = fb.getObject();
		runServerInThread(server);
		testServerWithFramedTransport(thriftServerPort);
		server.stop();
	}

	@Test
	public void testNonblockingServer() throws Exception {
		ThriftServerFactoryBean fb = setupFactoryForServer(ServerType.NONBLOCK_NB);
		int thriftServerPort = fb.getThriftServerPort();
		final TServer server = fb.getObject();
		runServerInThread(server);
		testServerWithFramedTransport(thriftServerPort);
		server.stop();
	}

	@Test
	public void testThreadedSelectorServer() throws Exception {
		ThriftServerFactoryBean fb = setupFactoryForServer(ServerType.NONBLOCK_TS);
		int thriftServerPort = fb.getThriftServerPort();
		final TServer server = fb.getObject();
		runServerInThread(server);
		testServerWithFramedTransport(thriftServerPort);
		server.stop();
	}

	@Test
	public void testThreadPoolServer() throws Exception {
		ThriftServerFactoryBean fb = setupFactoryForServer(ServerType.BLOCK_DEFAULT);
		int thriftServerPort = fb.getThriftServerPort();
		final TServer server = fb.getObject();
		runServerInThread(server);
		// blocking can't use TFramedTransport
		testServerWithTransport(thriftServerPort);
		server.stop();
	}

	private void testServerWithFramedTransport(int port) throws Exception {
		TTransport transport = new TFramedTransport(new TSocket("localhost", port, 2000));
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);

		ThriftTemplate<TestService.Client> template =
				new ThriftTemplate<TestService.Client>(TestService.class, protocol);
		template.afterPropertiesSet();

		Long ret = template.executeClient(new ThriftCallback<Long, TestService.Client>() {
			@Override
			public Long doInThrift(Client proxy) throws TException {
				return proxy.add(1, 1);
			}
		});

		assertThat(ret, is(2l));
	}

	private void testServerWithTransport(int port) throws Exception {
		TTransport transport = new TSocket("localhost", port, 2000);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);

		ThriftTemplate<TestService.Client> template =
				new ThriftTemplate<TestService.Client>(TestService.class, protocol);
		template.afterPropertiesSet();

		Long ret = template.executeClient(new ThriftCallback<Long, TestService.Client>() {
			@Override
			public Long doInThrift(Client proxy) throws TException {
				return proxy.add(1, 1);
			}
		});

		assertThat(ret, is(2l));
	}

	private ThriftServerFactoryBean setupFactoryForServer(ServerType serverType) throws Exception {
		ThriftServerFactoryBean fb = new ThriftServerFactoryBean();
		AddTestService addTestService = new AddTestService();
		Processor<AddTestService> processor = new TestService.Processor<AddTestService>(addTestService);
		fb.setProcessor(processor);
		fb.setServerType(serverType);
		fb.afterPropertiesSet();
		return fb;
	}

	private void runServerInThread(final TServer server) {
		Runnable runnable = new Runnable() {
			@Override public void run() {
				server.serve();
			}
		};
		Thread t1 = new Thread(runnable);
		t1.start();
	}

}
