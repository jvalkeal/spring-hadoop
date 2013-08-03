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

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfigBuilder;
import org.springframework.yarn.fs.ResourceLocalizer;

@Configuration
public class SpringYarnConfiguration implements ImportAware, BeanClassLoaderAware {

	private SpringYarnConfigBuilder builder = new SpringYarnConfigBuilder(true);
	private List<SpringYarnConfigurer<SpringYarnConfigBuilder>> configurers;

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
	}

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
	}

	@Autowired(required = false)
	public void setConfigurers(List<SpringYarnConfigurer<SpringYarnConfigBuilder>> configurers) throws Exception {
		for (SpringYarnConfigurer<SpringYarnConfigBuilder> configurer : configurers) {
			builder.apply(configurer);
		}
		this.configurers = configurers;
	}

	@Bean(name=YarnSystemConstants.DEFAULT_ID_CONFIGURATION)
	public YarnConfiguration yarnConfiguration() throws Exception {
//		boolean hasConfigurers = yarnConfigConfigurers != null && !yarnConfigConfigurers.isEmpty();
//		if(!hasConfigurers) {
//            throw new IllegalStateException("At least one non-null instance of "
//            		+ YarnConfigurer.class.getSimpleName()
//            		+" must be exposed as a @Bean when using @EnableYarn. Hint try extending "
//            		+ YarnConfigConfigurerAdapter.class.getSimpleName());
//		}
//		return yarnConfigBuilder.build();
		SpringYarnConfig config = builder.build();
		return (YarnConfiguration) config.getConfiguration();
	}


	@Bean(name=YarnSystemConstants.DEFAULT_ID_LOCAL_RESOURCES)
	@DependsOn(YarnSystemConstants.DEFAULT_ID_CONFIGURATION)
	public ResourceLocalizer yarnLocalresources() throws Exception {
//		return yarnResourceLocalizerBuilder.build();
		ResourceLocalizer localizer = builder.getOrBuild().getLocalizer();
		return localizer;
	}


}
