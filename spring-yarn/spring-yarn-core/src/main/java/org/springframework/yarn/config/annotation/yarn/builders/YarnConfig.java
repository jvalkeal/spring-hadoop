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
import org.springframework.core.io.Resource;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.configurers.PropertiesConfigureAware;
import org.springframework.yarn.config.annotation.configurers.PropertiesConfigurer;
import org.springframework.yarn.config.annotation.configurers.ResourceConfigureAware;
import org.springframework.yarn.config.annotation.configurers.ResourceConfigurer;
import org.springframework.yarn.configuration.ConfigurationFactoryBean;

/**
 * {@link AnnotationBuilder} for {@link YarnConfiguration}.
 *
 * @author Janne Valkealahti
 *
 */
public final class YarnConfig extends AbstractConfiguredAnnotationBuilder<YarnConfiguration, YarnConfig>
		implements PropertiesConfigureAware, ResourceConfigureAware {

	private final Set<Resource> resources = new HashSet<Resource>();
	private final Properties properties = new Properties();

	private String fileSystemUri;

	public YarnConfig() {
		this(true);
	}

	public YarnConfig(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	@Override
	protected YarnConfiguration performBuild() throws Exception {
		ConfigurationFactoryBean fb = new ConfigurationFactoryBean();

		fb.setResources(resources);
		fb.setProperties(properties);
		fb.setFileSystemUri(fileSystemUri);

		fb.afterPropertiesSet();
		return fb.getObject();
	}

	@Override
	public void configureProperties(Properties properties) {
		getProperties().putAll(properties);
	}

	@Override
	public void configureResources(Set<Resource> resources) {
		getResources().addAll(resources);
	}

	public ResourceConfigurer<YarnConfiguration, YarnConfig> withResources() throws Exception {
		return apply(new ResourceConfigurer<YarnConfiguration, YarnConfig>());
	}

	public Properties getProperties() {
		return properties;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public PropertiesConfigurer<YarnConfiguration, YarnConfig> withProperties() throws Exception {
		return apply(new PropertiesConfigurer<YarnConfiguration, YarnConfig>());
	}

	public YarnConfig fileSystemUri(String uri) {
		fileSystemUri = uri;
		return this;
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
