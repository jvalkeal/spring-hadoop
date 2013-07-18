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
package org.springframework.yarn.thrift.support;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.Assert;
import org.springframework.yarn.support.LifecycleObjectSupport;

/**
 * Base support class for service components
 * sharing some common functionality.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class ThriftObjectSupport extends LifecycleObjectSupport {

	private final static Log log = LogFactory.getLog(ThriftObjectSupport.class);

	/** Default lower range for free port scan */
	private final static int DEFAULT_MIN_FREE_PORT = 8200;

	/** Default upper range for free port scan */
	private final static int DEFAULT_MAX_FREE_PORT = 8300;

	/** Lower range for free port scan */
	private int minFreeServerPort = DEFAULT_MIN_FREE_PORT;

	/** Upper range for free port scan */
	private int maxFreeServerPort = DEFAULT_MAX_FREE_PORT;

	/** Either explicitly used port or set during the scan */
	private volatile int thriftServerPort = -1;

	/** Flag if thrift transport is started */
	private volatile boolean thriftServerStarted;

	/**
	 * Gets the thrift server port.
	 *
	 * @return the thrift server port
	 */
	public int getThriftServerPort() {
		return thriftServerPort;
	}

	/**
	 * Checks if is thrift server is started.
	 *
	 * @return true, if is thrift server is started
	 */
	public boolean isThriftServerStarted() {
		return thriftServerStarted;
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
	 * Sets the port range.
	 *
	 * @param minPort the min port
	 * @param maxPort the max port
	 */
	public void setPortRange(int minPort, int maxPort) {
		Assert.isTrue(minPort < maxPort, "minPort must be lower than maxPort");
		minFreeServerPort = minPort;
		maxFreeServerPort = maxPort;
	}

	/**
	 * Gets the processor.
	 *
	 * @return the processor
	 */
	protected abstract TProcessor getProcessor();

	/**
	 * Creates the server transport.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void createServerTransport() throws IOException {
		TaskExecutor taskExecutor = getTaskExecutor();
		Assert.notNull(taskExecutor, "Task Executor must be available");

		TNonblockingServerTransport transport = getFreeTransport();
		THsHaServer.Args args = new THsHaServer.Args(transport);
		args.transportFactory(new TFramedTransport.Factory());
		args.protocolFactory(new TBinaryProtocol.Factory());
		args.processor(getProcessor());
		args.workerThreads(1);
		final THsHaServer server = new THsHaServer(args);

		// serve is blocking so pass it to executor
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				server.serve();
			}
		});
	}

	/**
	 * Gets the free transport. This method either uses explicit port set
	 * using {@link #setThriftServerPort(int)} or tries to find a free port
	 * set using method {@link #setPortRange(int, int)}.
	 *
	 * @return the transport
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private TNonblockingServerTransport getFreeTransport() throws IOException {
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
				log.info("Starting thrift server on port=" + p);
				thriftServerStarted = true;
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

}
