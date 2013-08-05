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
import org.springframework.yarn.config.annotation.yarn.SpringYarnConfigs;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * {@link AnnotationBuilder} for {@link SpringYarnConfigs}.
 *
 * @author Janne Valkealahti
 *
 */
public class SpringYarnConfig extends AbstractConfiguredAnnotationBuilder<SpringYarnConfigs, SpringYarnConfig> {

	private YarnConfig yarnConfig;

	private YarnResourceLocalizer resourceLocalizer;

	public SpringYarnConfig() {
		this(true);
	}

	public SpringYarnConfig(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	@Override
	protected SpringYarnConfigs performBuild() throws Exception {
		SpringYarnConfigs config = new SpringYarnConfigs();
		YarnConfiguration configuration = (YarnConfiguration) yarnConfig.build();
		config.setConfiguration(configuration);

		resourceLocalizer.configuration(configuration);
		ResourceLocalizer localizer = resourceLocalizer.build();
		config.setLocalizer(localizer);

		return config;
	}

	public void setYarnConfigBuilder(YarnConfig yarnConfigBuilder) {
		this.yarnConfig = yarnConfigBuilder;
	}

	public void setYarnLocalizerBuilder(YarnResourceLocalizer resourceLocalizerBuilder) {
		this.resourceLocalizer = resourceLocalizerBuilder;
	}

}
