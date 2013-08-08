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
package org.springframework.yarn.config.annotation.yarn;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.yarn.client.YarnClient;
import org.springframework.yarn.config.annotation.yarn.EnableYarn.Enable;
import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnAppmasterBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnClientBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnContainerBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnEnvironmentBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnResourceLocalizerBuilder;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Provides a convenient base class for creating a {@link SpringYarnConfigurer}
 * instance. The implementation allows customization by overriding methods.
 *
 * @author Janne Valkealahti
 * @see EnableYarn
 */
public class SpringYarnConfigurerAdapter implements SpringYarnConfigurer<SpringYarnConfigBuilder> {

	private final static Log log = LogFactory.getLog(SpringYarnConfigurerAdapter.class);

	private YarnConfigBuilder yarnConfigBuilder;
	private YarnResourceLocalizerBuilder yarnResourceLocalizerBuilder;
	private YarnEnvironmentBuilder yarnEnvironmentBuilder;
	private YarnClientBuilder yarnClientBuilder;
	private YarnAppmasterBuilder yarnAppmasterBuilder;
	private YarnContainerBuilder yarnContainerBuilder;

	@Override
	public void init(SpringYarnConfigBuilder builder) throws Exception {
		builder.setYarnConfigBuilder(getConfigBuilder());
		builder.setYarnLocalizerBuilder(getLocalizerBuilder());
		builder.setEnvironmentBuilder(getEnvironmentBuilder());

		EnableYarn annotation = AnnotationUtils.findAnnotation(getClass(), EnableYarn.class);
		Enable enable = annotation.enable();

		if (log.isDebugEnabled()) {
			log.debug("Enabling " + enable);
		}

		if (enable == Enable.CLIENT) {
			builder.setYarnClientBuilder(getClientBuilder());
		} else if (enable == Enable.APPMASTER) {
			builder.setYarnAppmasterBuilder(getAppmasterBuilder());
		} else if (enable == Enable.CONTAINER) {
			builder.setYarnContainerBuilder(getContainerBuilder());
		}
	}

	@Override
	public void configure(SpringYarnConfigBuilder builder) throws Exception {
	}

	/**
	 * Configure {@link YarnConfiguration} via {@link YarnConfigBuilder} builder.
	 *
	 * @param config the {@link YarnConfiguration} builder
	 * @throws Exception if error occurred
	 */
	protected void configure(YarnConfigBuilder config) throws Exception {
	}

	/**
	 * Configure {@link ResourceLocalizer} via {@link YarnResourceLocalizerBuilder} builder.
	 *
	 * @param config the {@link ResourceLocalizer} builder
	 * @throws Exception if error occurred
	 */
	protected void configure(YarnResourceLocalizerBuilder localizer) throws Exception {
	}

	/**
	 * Configure {@link Map} of environment via {@link YarnEnvironmentBuilder} builder.
	 *
	 * @param environment the {@link YarnEnvironmentBuilder} builder
	 * @throws Exception if error occurred
	 */
	protected void configure(YarnEnvironmentBuilder environment) throws Exception {
	}

	/**
	 * Configure {@link YarnClient} of environment via {@link YarnClientBuilder} builder.
	 *
	 * @param client the {@link YarnClientBuilder} builder
	 * @throws Exception if error occurred
	 */
	protected void configure(YarnClientBuilder client) throws Exception {
	}

	protected void configure(YarnAppmasterBuilder master) throws Exception {
	}

	protected void configure(YarnContainerBuilder container) throws Exception {
	}

	protected final YarnConfigBuilder getConfigBuilder() throws Exception {
		if (yarnConfigBuilder != null) {
			return yarnConfigBuilder;
		}
		yarnConfigBuilder = new YarnConfigBuilder(true);
		configure(yarnConfigBuilder);
		return yarnConfigBuilder;
	}

	protected final YarnResourceLocalizerBuilder getLocalizerBuilder() throws Exception {
		if (yarnResourceLocalizerBuilder != null) {
			return yarnResourceLocalizerBuilder;
		}
		yarnResourceLocalizerBuilder = new YarnResourceLocalizerBuilder(true);
		configure(yarnResourceLocalizerBuilder);
		return yarnResourceLocalizerBuilder;
	}

	protected final YarnEnvironmentBuilder getEnvironmentBuilder() throws Exception {
		if (yarnEnvironmentBuilder != null) {
			return yarnEnvironmentBuilder;
		}
		yarnEnvironmentBuilder = new YarnEnvironmentBuilder(true);
		configure(yarnEnvironmentBuilder);
		return yarnEnvironmentBuilder;
	}

	protected final YarnClientBuilder getClientBuilder() throws Exception {
		if (yarnClientBuilder != null) {
			return yarnClientBuilder;
		}
		yarnClientBuilder = new YarnClientBuilder(true);
		configure(yarnClientBuilder);
		return yarnClientBuilder;
	}

	protected final YarnAppmasterBuilder getAppmasterBuilder() throws Exception {
		if (yarnAppmasterBuilder != null) {
			return yarnAppmasterBuilder;
		}
		yarnAppmasterBuilder = new YarnAppmasterBuilder(true);
		configure(yarnAppmasterBuilder);
		return yarnAppmasterBuilder;
	}

	protected final YarnContainerBuilder getContainerBuilder() throws Exception {
		if (yarnContainerBuilder != null) {
			return yarnContainerBuilder;
		}
		yarnContainerBuilder = new YarnContainerBuilder(true);
		configure(yarnContainerBuilder);
		return yarnContainerBuilder;
	}

}
