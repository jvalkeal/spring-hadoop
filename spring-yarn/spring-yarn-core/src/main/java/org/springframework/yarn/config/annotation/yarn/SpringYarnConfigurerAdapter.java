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

import org.springframework.yarn.config.annotation.yarn.builders.YarnEnvironment;
import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfig;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfig;
import org.springframework.yarn.config.annotation.yarn.builders.YarnResourceLocalizer;

/**
 * Provides a convenient base class for creating a {@link SpringYarnConfigurer}
 * instance. The implementation allows customization by overriding methods.
 *
 * @author Janne Valkealahti
 * @see EnableYarn
 */
public class SpringYarnConfigurerAdapter implements SpringYarnConfigurer<SpringYarnConfig> {

	private YarnConfig configBuilder;
	private YarnResourceLocalizer resourceLocalizerBuilder;
	private YarnEnvironment environmentBuilder;

	@Override
	public void init(SpringYarnConfig builder) throws Exception {
		final YarnConfig configBuilder = getConfigBuilder();
		builder.setYarnConfigBuilder(configBuilder);

		final YarnResourceLocalizer resourceLocalizerBuilder = getLocalizerBuilder();
		builder.setYarnLocalizerBuilder(resourceLocalizerBuilder);

		final YarnEnvironment environmentBuilder = getEnvironmentBuilder();
		builder.setEnvironmentBuilder(environmentBuilder);
	}

	@Override
	public void configure(SpringYarnConfig builder) throws Exception {
	}

	/**
	 * Configure {@link YarnConfiguration} via {@link YarnConfig} builder.
	 *
	 * @param config the {@link YarnConfiguration} builder
	 * @throws Exception if error occurred
	 */
	protected void configure(YarnConfig config) throws Exception {
	}

	/**
	 * Configure {@link ResourceLocalizer} via {@link YarnResourceLocalizer} builder.
	 *
	 * @param config the {@link ResourceLocalizer} builder
	 * @throws Exception if error occurred
	 */
	protected void configure(YarnResourceLocalizer localizer) throws Exception {
	}

	/**
	 * Configure {@link Map} of environment via {@link YarnEnvironment} builder.
	 *
	 * @param environment the {@link YarnEnvironment} builder
	 * @throws Exception if error occurred
	 */
	protected void configure(YarnEnvironment environment) throws Exception {
	}

	protected final YarnConfig getConfigBuilder() throws Exception {
		if (configBuilder != null) {
			return configBuilder;
		}
		configBuilder = new YarnConfig(true);
		configure(configBuilder);
		return configBuilder;
	}

	protected final YarnResourceLocalizer getLocalizerBuilder() throws Exception {
		if (resourceLocalizerBuilder != null) {
			return resourceLocalizerBuilder;
		}
		resourceLocalizerBuilder = new YarnResourceLocalizer(true);
		configure(resourceLocalizerBuilder);
		return resourceLocalizerBuilder;
	}

	protected final YarnEnvironment getEnvironmentBuilder() throws Exception {
		if (environmentBuilder != null) {
			return environmentBuilder;
		}
		environmentBuilder = new YarnEnvironment(true);
		configure(environmentBuilder);
		return environmentBuilder;
	}

}
