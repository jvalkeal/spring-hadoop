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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Factory bean building {@link TServer} instances.
 *
 * @author Janne Valkealahti
 *
 */
public class ThriftServerFactoryBean implements InitializingBean, FactoryBean<TServer> {

	private final static Log log = LogFactory.getLog(ThriftServerFactoryBean.class);

	/** Default lower range for free port scan */
	public final static int DEFAULT_MIN_FREE_PORT = 8200;

	/** Default upper range for free port scan */
	public final static int DEFAULT_MAX_FREE_PORT = 8300;

	/** Server returned from this factory */
	private TServer server;

	/** Type of server to build from this factory */
	private ServerType serverType = ServerType.NONBLOCK_HSHA;

	/** Processor to register for the server*/
	private TProcessor processor;

	/** Lower range for free port scan */
	private int minFreeServerPort = DEFAULT_MIN_FREE_PORT;

	/** Upper range for free port scan */
	private int maxFreeServerPort = DEFAULT_MAX_FREE_PORT;

	/** Either explicitly used port or set during the scan */
	private volatile int thriftServerPort = -1;

	@Override
	public TServer getObject() throws Exception {
		return server;
	}

	@Override
	public Class<TServer> getObjectType() {
		return TServer.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(processor, "Thrift processor must be set");
		if (serverType == ServerType.NONBLOCK_HSHA) {
			server = buildHsHaServer();
		} else if (serverType == ServerType.BLOCK_DEFAULT) {
			server = buildThreadPoolServer();
		} else if (serverType == ServerType.NONBLOCK_NB) {
			server = buildNonblockingServer();
		} else if (serverType == ServerType.NONBLOCK_TS) {
			server = buildThreadedSelectorServer();
		}
		Assert.notNull(server, "Unknown ServerType " + serverType);
	}

	/**
	 * Sets the thrift processor.
	 *
	 * @param processor the new processor
	 */
	public void setProcessor(TProcessor processor) {
		this.processor = processor;
	}

	/**
	 * Sets the server type.
	 *
	 * @param serverType the new server type
	 */
	public void setServerType(ServerType serverType) {
		Assert.notNull(serverType, "ServerType must not be null");
		this.serverType = serverType;
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
	 * Sets the thrift server port.
	 *
	 * @param thriftServerPort the new thrift server port
	 */
	public void setThriftServerPort(int thriftServerPort) {
		this.thriftServerPort = thriftServerPort;
	}

	/**
	 * Sets the lower free server port scan range.
	 *
	 * @param minFreeServerPort the new lower port scan range
	 */
	public void setMinFreeServerPort(int minFreeServerPort) {
		this.minFreeServerPort = minFreeServerPort;
	}

	/**
	 * Sets the upper free server port scan range.
	 *
	 * @param setMaxFreeServerPort the new upper port scan range
	 */
	public void setMaxFreeServerPort(int maxFreeServerPort) {
		this.maxFreeServerPort = maxFreeServerPort;
	}

	/**
	 * Builds the hs ha server.
	 *
	 * @return the t hs ha server
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private THsHaServer buildHsHaServer() throws IOException {
		TNonblockingServerTransport transport = getNonblockingServerSocket();
		THsHaServer.Args args = new THsHaServer.Args(transport);
		args.transportFactory(new TFramedTransport.Factory());
		args.protocolFactory(new TBinaryProtocol.Factory());
		args.processor(processor);
		args.workerThreads(1);
		return new THsHaServer(args);
	}

	/**
	 * Builds the thread pool server.
	 *
	 * @return the t thread pool server
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private TThreadPoolServer buildThreadPoolServer() throws IOException {
		TServerTransport transport = getBlockingServerSocket();
		TThreadPoolServer.Args args = new TThreadPoolServer.Args(transport);
		args.processor(processor);
		return new TThreadPoolServer(args);
	}

	/**
	 * Builds the nonblocking server.
	 *
	 * @return the t nonblocking server
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private TNonblockingServer buildNonblockingServer() throws IOException {
		TNonblockingServerTransport transport = getNonblockingServerSocket();
		TNonblockingServer.Args args = new TNonblockingServer.Args(transport);
		args.transportFactory(new TFramedTransport.Factory());
		args.protocolFactory(new TBinaryProtocol.Factory());
		args.processor(processor);
		return new TNonblockingServer(args);
	}

	/**
	 * Builds the threaded selector server.
	 *
	 * @return the t threaded selector server
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private TThreadedSelectorServer buildThreadedSelectorServer() throws IOException {
		TNonblockingServerTransport transport = getNonblockingServerSocket();
		TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(transport);
		args.transportFactory(new TFramedTransport.Factory());
		args.protocolFactory(new TBinaryProtocol.Factory());
		args.processor(processor);
		args.workerThreads(1);
		return new TThreadedSelectorServer(args);
	}

	/**
	 * Gets the free transport. This method either uses explicit port set
	 * using {@link #setThriftServerPort(int)} or tries to find a free port
	 * set using method {@link #setPortRange(int, int)}.
	 *
	 * @return the transport
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private TServerTransport getBlockingServerSocket() throws IOException {
		int minPort;
		int maxPort;

		if (thriftServerPort < 0) {
			minPort = minFreeServerPort;
			maxPort = maxFreeServerPort;
		} else {
			minPort = thriftServerPort;
			maxPort = thriftServerPort;
		}

		for (int p = minPort; p <= maxPort; p++) {
			try {
				TServerSocket t = new TServerSocket(p);
				log.info("Trying thrift server socket on port=" + p);
				thriftServerPort = p;
				return t;
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("Could not create server socket on port=" + p);
				}
				continue;
			}
		}
		throw new IOException("No free ports available from [" + minPort + " to " + maxPort + "]");
	}

	/**
	 * Gets the free transport. This method either uses explicit port set
	 * using {@link #setThriftServerPort(int)} or tries to find a free port
	 * set using method {@link #setPortRange(int, int)}.
	 *
	 * @return the transport
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private TNonblockingServerTransport getNonblockingServerSocket() throws IOException {
		int minPort;
		int maxPort;

		if (thriftServerPort < 0) {
			minPort = minFreeServerPort;
			maxPort = maxFreeServerPort;
		} else {
			minPort = thriftServerPort;
			maxPort = thriftServerPort;
		}

		for (int p = minPort; p <= maxPort; p++) {
			try {
				TNonblockingServerSocket t = new TNonblockingServerSocket(p);
				log.info("Trying thrift server socket on port=" + p);
				thriftServerPort = p;
				return t;
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("Could not create server socket on port=" + p);
				}
				continue;
			}
		}
		throw new IOException("No free ports available from [" + minPort + " to " + maxPort + "]");
	}

	/**
	 * The Enum ServerType.
	 */
	public enum ServerType {
		BLOCK_DEFAULT,
		NONBLOCK_NB,
		NONBLOCK_HSHA,
		NONBLOCK_TS
	}

}
