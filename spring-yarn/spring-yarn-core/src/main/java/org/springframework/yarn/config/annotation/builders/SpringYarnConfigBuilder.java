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
package org.springframework.yarn.config.annotation.builders;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.data.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.SpringYarnConfigs;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * {@link org.springframework.data.config.annotation.AnnotationBuilder AnnotationBuilder}
 * for {@link SpringYarnConfigs}.
 *
 * @author Janne Valkealahti
 *
 */
public class SpringYarnConfigBuilder extends AbstractConfiguredAnnotationBuilder<SpringYarnConfigs, SpringYarnConfigBuilder> {

	private Configuration yarnConfiguration;

	public SpringYarnConfigBuilder() {
	}

	@Override
	protected SpringYarnConfigs performBuild() throws Exception {
		SpringYarnConfigs config = new SpringYarnConfigs();

		Configuration configuration = (yarnConfiguration == null)
				? (YarnConfiguration) getSharedObject(YarnConfigBuilder.class).build()
				: yarnConfiguration;

		config.setConfiguration(configuration);

		YarnResourceLocalizerBuilder yarnResourceLocalizerBuilder = getSharedObject(YarnResourceLocalizerBuilder.class);
		yarnResourceLocalizerBuilder.configuration(configuration);
		ResourceLocalizer localizer = yarnResourceLocalizerBuilder.build();
		config.setLocalizer(localizer);

		Map<String, String> env = getSharedObject(YarnEnvironmentBuilder.class).build();
		config.setEnvironment(env);

		YarnClientBuilder yarnClientBuilder = getSharedObject(YarnClientBuilder.class);
		if (yarnClientBuilder != null) {
			yarnClientBuilder.configuration(configuration);
			yarnClientBuilder.setResourceLocalizer(localizer);
			yarnClientBuilder.setEnvironment(env);
			config.setYarnClient(yarnClientBuilder.build());
		}

		YarnAppmasterBuilder yarnAppmasterBuilder = getSharedObject(YarnAppmasterBuilder.class);
		if (yarnAppmasterBuilder != null) {
			yarnAppmasterBuilder.configuration(configuration);
			yarnAppmasterBuilder.setResourceLocalizer(localizer);
			yarnAppmasterBuilder.setEnvironment(env);
			config.setYarnAppmaster(yarnAppmasterBuilder.build());
		}

		YarnContainerBuilder yarnContainerBuilder = getSharedObject(YarnContainerBuilder.class);
		if (yarnContainerBuilder != null) {
			config.setYarnContainer(yarnContainerBuilder.build());
		}

		return config;
	}

	public void setYarnConfiguration(Configuration yarnConfiguration) {
		this.yarnConfiguration = yarnConfiguration;
	}

}
