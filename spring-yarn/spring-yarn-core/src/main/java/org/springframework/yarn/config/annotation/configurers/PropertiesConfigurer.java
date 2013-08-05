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
package org.springframework.yarn.config.annotation.configurers;

import java.util.Properties;

import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;

/**
 *
 * @author Janne Valkealahti
 *
 * @param <T>
 * @param <O>
 * @param <B>
 *
 * abstract class PropertiesConfigurer <T extends AnnotationBuilder<O>, O, B extends AnnotationBuilder<O>>
 */
public class PropertiesConfigurer <O, B extends AnnotationBuilder<O>>
		extends AnnotationConfigurerAdapter<O,B> {

	private Properties properties = new Properties();

	public PropertiesConfigurer<O,B> add(Properties properties) {
		properties.putAll(properties);
		return this;
	}

	public PropertiesConfigurer<O,B> add(String key, String value) {
		properties.put(key, value);
		return this;
	}

	public Properties getProperties() {
		return properties;
	}

	@Override
	public void configure(B builder) throws Exception {
		if (!configureProperties(builder, properties)) {
			if (builder instanceof PropertiesConfigureAware) {
				((PropertiesConfigureAware)builder).configureProperties(properties);
			}
		}
	}

	protected boolean configureProperties(B builder, Properties properties){
		return false;
	};

}
