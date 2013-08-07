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

import java.util.Map;

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
public class SpringYarnConfigBuilder extends AbstractConfiguredAnnotationBuilder<SpringYarnConfigs, SpringYarnConfigBuilder> {

	private YarnConfigBuilder yarnConfig;
	private YarnResourceLocalizerBuilder resourceLocalizer;
	private YarnEnvironmentBuilder environment;
	private YarnClientBuilder yarnClientBuilder;
	private YarnAppmasterBuilder yarnAppmasterBuilder;
	private YarnContainerBuilder yarnContainerBuilder;

	public SpringYarnConfigBuilder() {
		this(true);
	}

	public SpringYarnConfigBuilder(boolean allowConfigurersOfSameType) {
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

		Map<String, String> env = environment.build();
		config.setEnvironment(env);

		if (yarnClientBuilder != null) {
			yarnClientBuilder.configuration(configuration);
			config.setYarnClient(yarnClientBuilder.build());
		}

		return config;
	}

	public void setYarnConfigBuilder(YarnConfigBuilder yarnConfigBuilder) {
		this.yarnConfig = yarnConfigBuilder;
	}

	public void setYarnLocalizerBuilder(YarnResourceLocalizerBuilder resourceLocalizerBuilder) {
		this.resourceLocalizer = resourceLocalizerBuilder;
	}

	public void setEnvironmentBuilder(YarnEnvironmentBuilder environmentBuilder) {
		this.environment = environmentBuilder;
	}

	public void setYarnClientBuilder(YarnClientBuilder yarnClientBuilder) {
		this.yarnClientBuilder = yarnClientBuilder;
	}

	public void setYarnAppmasterBuilder(YarnAppmasterBuilder yarnAppmasterBuilder) {
		this.yarnAppmasterBuilder = yarnAppmasterBuilder;
	}

	public void setYarnContainerBuilder(YarnContainerBuilder yarnContainerBuilder) {
		this.yarnContainerBuilder = yarnContainerBuilder;
	}

}
