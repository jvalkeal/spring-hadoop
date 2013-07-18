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

import org.apache.thrift.TException;
import org.springframework.dao.UncategorizedDataAccessException;

/**
 * General exception indicating a problem in components interacting
 * with thrift. Main point of wrapping native thrift exceptions inside
 * this is to have common Spring dao exception hierarchy.
 *
 * @author Janne Valkealahti
 *
 */
public class ThriftSystemException extends UncategorizedDataAccessException {

	private static final long serialVersionUID = 8864640104497724021L;

	/**
	 * Instantiates a new thrift system exception.
	 *
	 * @param e the thrift exception
	 */
	public ThriftSystemException(TException e) {
		super(e.getMessage(), e);
	}

	/**
	 * Instantiates a new thrift system exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ThriftSystemException(String message, Throwable cause) {
		super(message, cause);
	}

}
