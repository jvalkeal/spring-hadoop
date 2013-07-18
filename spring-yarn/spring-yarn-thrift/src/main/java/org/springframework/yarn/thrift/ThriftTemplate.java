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
 * @param <P> the type of the api
 */
public class ThriftTemplate<P> implements InitializingBean {

	private Class<?> apiClass;
	//private Class<?> proxyProcessorClass;
	private Class<?> proxyClientClass;
	//private Class<?> proxyIfaceClass;
	private TProtocol protocol;
	private P proxy;

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
		//proxyProcessorClass = Class.forName(apiClass.getName() + "$Processor");
		proxyClientClass = Class.forName(apiClass.getName() + "$Client");
		//proxyIfaceClass = Class.forName(apiClass.getName() + "$Iface");
		proxy = createProxy();
	}

	/**
	 * Execute client.
	 *
	 * @param <T> the callback return type
	 * @param <P> the proxy type
	 * @param action the callback
	 * @return the t
	 * @throws DataAccessException the data access exception
	 */
	@SuppressWarnings("hiding")
	public <T, P> T executeClient(ThriftCallback<T, P> action) throws DataAccessException {
		try {
			@SuppressWarnings("unchecked")
			P proxy = (P) getProxy();
			T result = action.doInThrift(proxy);
			return result;
		} catch (TException e) {
			throw new ThriftSystemException(e);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * Creates proxy for thrift generated client api.
	 *
	 * @return the proxy for client api
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 */
	@SuppressWarnings("unchecked")
	protected P createProxy() throws InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException {
		Constructor<?> constructor = proxyClientClass.getConstructor(TProtocol.class);
		return (P) BeanUtils.instantiateClass(constructor, protocol);
	}

	/**
	 * Gets the proxy.
	 *
	 * @return the proxy
	 */
	protected P getProxy() {
		return proxy;
	}

}
