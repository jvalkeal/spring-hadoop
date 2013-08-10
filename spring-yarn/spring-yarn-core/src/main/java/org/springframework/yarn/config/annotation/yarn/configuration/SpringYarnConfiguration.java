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
package org.springframework.yarn.config.annotation.yarn.configuration;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.config.annotation.AbstractAnnotationConfiguration;
import org.springframework.yarn.config.annotation.AnnotationConfigurer;
import org.springframework.yarn.config.annotation.yarn.EnableYarn;
import org.springframework.yarn.config.annotation.yarn.EnableYarn.Enable;
import org.springframework.yarn.config.annotation.yarn.SpringYarnConfigs;
import org.springframework.yarn.config.annotation.yarn.SpringYarnConfigurer;
import org.springframework.yarn.config.annotation.yarn.SpringYarnConfigurerAdapter;
import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfigBuilder;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Uses a {@link SpringYarnConfigBuilder} to create {@link SpringYarnConfigs}
 * holding all relevant configurations for Spring Yarn. It then exports the
 * necessary beans. Customizations can be made to {@link SpringYarnConfigBuilder} by
 * extending {@link SpringYarnConfigurerAdapter} and exposing it as a
 * {@link Configuration} or implementing {@link SpringYarnConfigurer} and
 * exposing it as a {@link Configuration}. This configuration is imported when
 * using {@link EnableYarn}.
 *
 * @author Janne Valkealahti
 * @see EnableYarn
 * @see SpringYarnConfigBuilder
 */
@Configuration
public class SpringYarnConfiguration extends AbstractAnnotationConfiguration<SpringYarnConfigBuilder, SpringYarnConfigs> {

	private final static Log log = LogFactory.getLog(SpringYarnConfiguration.class);

	protected SpringYarnConfigBuilder builder = new SpringYarnConfigBuilder();

	private List<SpringYarnConfigurer> configurers;

	@Bean(name=YarnSystemConstants.DEFAULT_ID_CONFIGURATION)
	public YarnConfiguration yarnConfiguration() throws Exception {
		SpringYarnConfigs config = builder.build();
		return (YarnConfiguration) config.getConfiguration();
	}

	@Autowired(required=false)
	public void setYarnConfiguration(org.apache.hadoop.conf.Configuration yarnConfiguration) {
		builder.setYarnConfiguration(yarnConfiguration);
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
	protected void onConfigurers(List<AnnotationConfigurer<SpringYarnConfigs, SpringYarnConfigBuilder>> configurers) throws Exception {
		for (AnnotationConfigurer<SpringYarnConfigs, SpringYarnConfigBuilder> configurer : configurers) {
			builder.apply(configurer);
		}
	}

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		super.setImportMetadata(importMetadata);

		Map<String, Object> enableYarnMap =
				importMetadata.getAnnotationAttributes(EnableYarn.class.getName());
		AnnotationAttributes enableYarnAttrs = AnnotationAttributes.fromMap(enableYarnMap);

		Enable enable = enableYarnAttrs.getEnum("enable");
		log.info("XXX enable: " + enable);
	}

}
