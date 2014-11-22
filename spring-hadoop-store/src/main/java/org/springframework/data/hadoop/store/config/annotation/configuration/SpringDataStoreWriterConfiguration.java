/*
 * Copyright 2014 the original author or authors.
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
package org.springframework.data.hadoop.store.config.annotation.configuration;

import java.lang.annotation.Annotation;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.common.annotation.AbstractImportingAnnotationConfiguration;
import org.springframework.data.hadoop.config.common.annotation.AnnotationConfigurer;
import org.springframework.data.hadoop.store.config.annotation.EnableDataStoreWriter;
import org.springframework.data.hadoop.store.config.annotation.SpringDataStoreWriterConfigs;
import org.springframework.data.hadoop.store.config.annotation.builders.SpringDataStoreWriterBuilder;
import org.springframework.util.ObjectUtils;

@Configuration
public class SpringDataStoreWriterConfiguration extends AbstractImportingAnnotationConfiguration<SpringDataStoreWriterBuilder, SpringDataStoreWriterConfigs> {

	private static final Log log = LogFactory.getLog(SpringDataStoreWriterConfiguration.class);

	private final SpringDataStoreWriterBuilder builder = new SpringDataStoreWriterBuilder();

	private final BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	@Override
	protected void onConfigurers(BeanDefinitionRegistry registry, String[] names,
			List<AnnotationConfigurer<SpringDataStoreWriterConfigs, SpringDataStoreWriterBuilder>> configurers)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("onConfigurers: " + configurers);
		}
		for (AnnotationConfigurer<SpringDataStoreWriterConfigs, SpringDataStoreWriterBuilder> configurer : configurers) {
			builder.apply(configurer);
		}
		BeanDefinition writerBean = builder.getOrBuild().getWriter();

		if (ObjectUtils.isEmpty(names)) {
			// ok, name(s) not given, generate one
			names = new String[]{beanNameGenerator.generateBeanName(writerBean, registry)};
		}

		registry.registerBeanDefinition(names[0], writerBean);
		if (names.length > 1) {
			for (int i = 1; i<names.length; i++) {
				registry.registerAlias(names[0], names[i]);
			}
		}
	}

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableDataStoreWriter.class;
	}

}
