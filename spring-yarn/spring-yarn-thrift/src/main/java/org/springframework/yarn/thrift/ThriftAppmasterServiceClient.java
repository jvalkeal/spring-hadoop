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

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.yarn.am.AppmasterServiceClient;
import org.springframework.yarn.thrift.support.ThriftObjectSupport;

/**
 * General base client service class used to communicate
 * with a thrift based service running in an application master.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class ThriftAppmasterServiceClient extends ThriftObjectSupport
		implements AppmasterServiceClient {

	/** Thrift server hostname */
	private String serverHost;

	/** Thrift server port */
	private int serverPort;

	/** Transport timeout */
	private int timeout;

	/**
	 * Gets the server host.
	 *
	 * @return the server host
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 * Sets the server host.
	 *
	 * @param serverHost the new server host
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * Gets the server port.
	 *
	 * @return the server port
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Sets the server port.
	 *
	 * @param serverPort the new server port
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Gets the timeout.
	 *
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Sets the timeout.
	 *
	 * @param timeout the new timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Gets the client thrift template.
	 *
	 * @param <T> the type of client class
	 * @param apiClass the main thrift class wrapping client api
	 * @param clientClass the client class
	 * @return the client thrift template
	 * @throws Exception the exception
	 */
	protected <T> ThriftTemplate<T> getClientThriftTemplate(Class<?> apiClass, Class<T> clientClass) throws Exception {
		TTransport transport = new TFramedTransport(new TSocket(getServerHost(), getServerPort(), getTimeout()));
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);

		ThriftTemplate<T> template =
				new ThriftTemplate<T>(apiClass, protocol);
		template.afterPropertiesSet();

		return template;
	}

}
