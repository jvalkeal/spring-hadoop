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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.yarn.SpringYarnConfigs;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * {@link org.springframework.yarn.config.annotation.AnnotationBuilder AnnotationBuilder}
 * for {@link SpringYarnConfigs}.
 *
 * @author Janne Valkealahti
 *
 */
public class SpringYarnConfigBuilder extends AbstractConfiguredAnnotationBuilder<SpringYarnConfigs, SpringYarnConfigBuilder> {

	private YarnConfigBuilder yarnConfigBuilder;
	private YarnResourceLocalizerBuilder yarnResourceLocalizerBuilder;
	private YarnEnvironmentBuilder yarnEnvironmentBuilder;
	private YarnClientBuilder yarnClientBuilder;
	private YarnAppmasterBuilder yarnAppmasterBuilder;
	private YarnContainerBuilder yarnContainerBuilder;

	private Configuration yarnConfiguration;

	public SpringYarnConfigBuilder() {
//		this(true);
	}

//	public SpringYarnConfigBuilder(boolean allowConfigurersOfSameType) {
//		super(allowConfigurersOfSameType);
//	}

	@Override
	protected SpringYarnConfigs performBuild() throws Exception {
		SpringYarnConfigs config = new SpringYarnConfigs();

		Configuration configuration = (yarnConfiguration == null)
				? (YarnConfiguration) yarnConfigBuilder.build()
				: yarnConfiguration;
		config.setConfiguration(configuration);

		yarnResourceLocalizerBuilder.configuration(configuration);
		ResourceLocalizer localizer = yarnResourceLocalizerBuilder.build();
		config.setLocalizer(localizer);

		Map<String, String> env = yarnEnvironmentBuilder.build();
		config.setEnvironment(env);

		if (yarnClientBuilder != null) {
			yarnClientBuilder.configuration(configuration);
			yarnClientBuilder.setResourceLocalizer(localizer);
			yarnClientBuilder.setEnvironment(env);
			config.setYarnClient(yarnClientBuilder.build());
		}

		if (yarnAppmasterBuilder != null) {
			yarnAppmasterBuilder.configuration(configuration);
			yarnAppmasterBuilder.setResourceLocalizer(localizer);
			yarnAppmasterBuilder.setEnvironment(env);
			config.setYarnAppmaster(yarnAppmasterBuilder.build());
		}

		if (yarnContainerBuilder != null) {
//			yarnContainerBuilder.configuration(configuration);
//			yarnContainerBuilder.setResourceLocalizer(localizer);
//			yarnContainerBuilder.setEnvironment(env);
			config.setYarnContainer(yarnContainerBuilder.build());
		}

		return config;
	}

	public void setYarnConfiguration(Configuration yarnConfiguration) {
		this.yarnConfiguration = yarnConfiguration;
	}

	public void setYarnConfigBuilder(YarnConfigBuilder yarnConfigBuilder) {
		this.yarnConfigBuilder = yarnConfigBuilder;
	}

	public void setYarnLocalizerBuilder(YarnResourceLocalizerBuilder yarnResourceLocalizerBuilder) {
		this.yarnResourceLocalizerBuilder = yarnResourceLocalizerBuilder;
	}

	public void setEnvironmentBuilder(YarnEnvironmentBuilder yarnEnvironmentBuilder) {
		this.yarnEnvironmentBuilder = yarnEnvironmentBuilder;
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
