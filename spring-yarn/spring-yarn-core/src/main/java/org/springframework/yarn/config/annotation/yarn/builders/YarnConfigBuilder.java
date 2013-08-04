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

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.yarn.configurers.YarnConfigPropertiesConfigurer;
import org.springframework.yarn.configuration.ConfigurationFactoryBean;

/**
 *
 *
 * @author Janne Valkealahti
 *
 * extends AbstractConfiguredAnnotationBuilder<YarnConfiguration, AnnotationBuilder<YarnConfiguration>>
 */
public final class YarnConfigBuilder
		extends AbstractConfiguredAnnotationBuilder<YarnConfiguration, YarnConfigBuilder> {

	private Set<Resource> resources = new HashSet<Resource>();
	private Properties properties = new Properties();
	private final DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

	public YarnConfigBuilder(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	@Override
	protected YarnConfiguration performBuild() throws Exception {
		ConfigurationFactoryBean fb = new ConfigurationFactoryBean();

		fb.setResources(resources);
		fb.setProperties(properties);

		fb.afterPropertiesSet();
		return fb.getObject();
	}

	public YarnConfigBuilder withResource(String resource) {
		resources.add(resourceLoader.getResource(resource));
		return this;
	}

	public Properties getProperties() {
		return properties;
	}

	// Bound mismatch: The generic method apply(C) of type AbstractConfiguredAnnotationBuilder<O,B> is not
	// applicable for the arguments (YarnConfigPropertiesConfigurer). The inferred type YarnConfigPropertiesConfigurer
	// is not a valid substitute for the bounded parameter
	// <C extends AnnotationConfigurerAdapter<YarnConfiguration,AnnotationBuilder<YarnConfiguration>>>

	public YarnConfigPropertiesConfigurer withProperties() throws Exception {
		YarnConfigPropertiesConfigurer configurer = new YarnConfigPropertiesConfigurer();
		return apply(configurer);
	}

//    private <C extends AnnotationConfigurerAdapter<YarnConfiguration, AnnotationBuilder<YarnConfiguration>>> C getOrApply(C configurer)
//            throws Exception {
//        C existingConfig = (C) getConfigurer(configurer.getClass());
//        if(existingConfig != null) {
//            return existingConfig;
//        }
//        return apply(configurer);
//    }


}
