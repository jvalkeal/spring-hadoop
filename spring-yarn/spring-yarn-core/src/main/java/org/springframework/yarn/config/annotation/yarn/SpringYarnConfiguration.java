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

import java.util.List;
import java.util.Map;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.config.annotation.AbstractAnnotationConfiguration;
import org.springframework.yarn.config.annotation.AnnotationConfigurer;
import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfig;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Uses a {@link SpringYarnConfig} to create {@link SpringYarnConfigs}
 * holding all relevant configuratins for Spring Yarn. It then exports the
 * necessary beans. Customizations can be made to {@link SpringYarnConfig} by
 * extending {@link SpringYarnConfigurerAdapter} and exposing it as a
 * {@link Configuration} or implementing {@link SpringYarnConfigurer} and
 * exposing it as a {@link Configuration}. This configuration is imported when
 * using {@link EnableYarn}.
 *
 * @author Janne Valkealahti
 * @see EnableYarn
 * @see SpringYarnConfig
 */
@Configuration
public class SpringYarnConfiguration extends AbstractAnnotationConfiguration<SpringYarnConfig, SpringYarnConfigs> {

	private SpringYarnConfig builder = new SpringYarnConfig(true);

	private List<SpringYarnConfigurer<SpringYarnConfig>> configurers;

	@Bean(name=YarnSystemConstants.DEFAULT_ID_CONFIGURATION)
	public YarnConfiguration yarnConfiguration() throws Exception {
		SpringYarnConfigs config = builder.build();
		return (YarnConfiguration) config.getConfiguration();
	}

	@Bean(name=YarnSystemConstants.DEFAULT_ID_LOCAL_RESOURCES)
	@DependsOn(YarnSystemConstants.DEFAULT_ID_CONFIGURATION)
	public ResourceLocalizer yarnLocalresources() throws Exception {
		return builder.getOrBuild().getLocalizer();
	}

	@Bean(name=YarnSystemConstants.DEFAULT_ID_ENVIRONMENT)
	@DependsOn(YarnSystemConstants.DEFAULT_ID_CONFIGURATION)
	public Map<String, String> yarnEnvironment() throws Exception {
		return builder.getOrBuild().getEnvironment();
	}

	@Override
	protected void onConfigurers(List<AnnotationConfigurer<SpringYarnConfigs, SpringYarnConfig>> configurers) throws Exception {
		for (AnnotationConfigurer<SpringYarnConfigs, SpringYarnConfig> configurer : configurers) {
			builder.apply(configurer);
		}
	}


}
