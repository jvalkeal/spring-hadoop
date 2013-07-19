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

import java.lang.reflect.Constructor;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

/**
 * Generic Spring template model for accessing thrift.
 *
 * @author Janne Valkealahti
 *
 * @param <C> the type of the thrift client
 */
public class ThriftTemplate<C> implements InitializingBean {

	private Class<?> apiClass;
	private Class<?> clientClass;
	private TProtocol protocol;
	private C client;

	/**
	 * Instantiates a new thrift template.
	 *
	 * @param apiClass the api class
	 * @param protocol the protocol
	 */
	public ThriftTemplate(Class<?> apiClass, TProtocol protocol) {
		this.apiClass = apiClass;
		this.protocol = protocol;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		clientClass = Class.forName(apiClass.getName() + "$Client");
		client = createClient();
	}

	/**
	 * Execute client.
	 *
	 * @param <T> the callback return type
	 * @param <P> the client type
	 * @param action the callback
	 * @return the result from a callback
	 * @throws DataAccessException the data access exception
	 */
	public <T, P> T executeClient(ThriftCallback<T, P> action) throws DataAccessException {
		try {
			@SuppressWarnings("unchecked")
			P client = (P) getClient();
			T result = action.doInThrift(client);
			return result;
		} catch (TException e) {
			throw new ThriftSystemException(e);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * Creates client for thrift generated api.
	 *
	 * @return the thrift client
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 */
	@SuppressWarnings("unchecked")
	protected C createClient() throws InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException {
		Constructor<?> constructor = clientClass.getConstructor(TProtocol.class);
		return (C) BeanUtils.instantiateClass(constructor, protocol);
	}

	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	private C getClient() {
		return client;
	}

}
