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
package org.springframework.yarn.config.annotation.yarn.builders;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.yarn.SpringYarnConfig;
import org.springframework.yarn.fs.ResourceLocalizer;

public class SpringYarnConfigBuilder extends AbstractConfiguredAnnotationBuilder<SpringYarnConfig, SpringYarnConfigBuilder> {

	private YarnConfigBuilder yarnConfigBuilder;
	private YarnResourceLocalizerBuilder resourceLocalizerBuilder;

	public SpringYarnConfigBuilder(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	@Override
	protected SpringYarnConfig performBuild() throws Exception {
		SpringYarnConfig config = new SpringYarnConfig();
		YarnConfiguration configuration = (YarnConfiguration) yarnConfigBuilder.build();
		config.setConfiguration(configuration);

		resourceLocalizerBuilder.configuration(configuration);
		ResourceLocalizer localizer = resourceLocalizerBuilder.build();
		config.setLocalizer(localizer);

		return config;
	}

	public void setYarnConfigBuilder(YarnConfigBuilder yarnConfigBuilder) {
		this.yarnConfigBuilder = yarnConfigBuilder;
	}

	public void setYarnLocalizerBuilder(YarnResourceLocalizerBuilder resourceLocalizerBuilder) {
		this.resourceLocalizerBuilder = resourceLocalizerBuilder;
	}

}
