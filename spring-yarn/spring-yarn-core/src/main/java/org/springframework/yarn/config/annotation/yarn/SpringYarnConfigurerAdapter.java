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

import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnResourceLocalizerBuilder;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public class SpringYarnConfigurerAdapter implements SpringYarnConfigurer<SpringYarnConfigBuilder> {

	private YarnConfigBuilder configBuilder;
	private YarnResourceLocalizerBuilder resourceLocalizerBuilder;

	@Override
	public void init(SpringYarnConfigBuilder builder) throws Exception {
		final YarnConfigBuilder configBuilder = getConfigBuilder();
		builder.setYarnConfigBuilder(configBuilder);

		final YarnResourceLocalizerBuilder resourceLocalizerBuilder = getLocalizerBuilder();
		builder.setYarnLocalizerBuilder(resourceLocalizerBuilder);
	}

	@Override
	public void configure(SpringYarnConfigBuilder builder) throws Exception {
	}

	protected void configure(YarnConfigBuilder config) throws Exception {
	}

	protected void configure(YarnResourceLocalizerBuilder localizer) throws Exception {
	}

	protected final YarnConfigBuilder getConfigBuilder() throws Exception {
		if (configBuilder != null) {
			return configBuilder;
		}

		configBuilder = new YarnConfigBuilder(true);
		configure(configBuilder);
		return configBuilder;
	}

	protected final YarnResourceLocalizerBuilder getLocalizerBuilder() throws Exception {
		if (resourceLocalizerBuilder != null) {
			return resourceLocalizerBuilder;
		}

		resourceLocalizerBuilder = new YarnResourceLocalizerBuilder(true);
		configure(resourceLocalizerBuilder);
		return resourceLocalizerBuilder;
	}

}
