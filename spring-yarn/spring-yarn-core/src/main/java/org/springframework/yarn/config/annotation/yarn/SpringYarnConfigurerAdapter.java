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

import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfig;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfig;
import org.springframework.yarn.config.annotation.yarn.builders.YarnResourceLocalizer;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public class SpringYarnConfigurerAdapter implements SpringYarnConfigurer<SpringYarnConfig> {

	private YarnConfig configBuilder;
	private YarnResourceLocalizer resourceLocalizerBuilder;

	@Override
	public void init(SpringYarnConfig builder) throws Exception {
		final YarnConfig configBuilder = getConfigBuilder();
		builder.setYarnConfigBuilder(configBuilder);

		final YarnResourceLocalizer resourceLocalizerBuilder = getLocalizerBuilder();
		builder.setYarnLocalizerBuilder(resourceLocalizerBuilder);
	}

	@Override
	public void configure(SpringYarnConfig builder) throws Exception {
	}

	protected void configure(YarnConfig config) throws Exception {
	}

	protected void configure(YarnResourceLocalizer localizer) throws Exception {
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

}
