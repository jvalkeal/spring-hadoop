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
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.common.annotation.AbstractImportingAnnotationConfiguration;
import org.springframework.data.hadoop.config.common.annotation.AnnotationConfigurer;
import org.springframework.data.hadoop.store.config.annotation.EnableDatasetWriter;
import org.springframework.data.hadoop.store.config.annotation.SpringDataStoreWriterConfigs;
import org.springframework.data.hadoop.store.config.annotation.builders.SpringDatasetWriterBuilder;

@Configuration
public class SpringDatasetWriterConfiguration extends
		AbstractImportingAnnotationConfiguration<SpringDatasetWriterBuilder, SpringDataStoreWriterConfigs> {

	private static final Log log = LogFactory.getLog(SpringDataStoreTextWriterConfiguration.class);

	private final SpringDatasetWriterBuilder builder = new SpringDatasetWriterBuilder();

	@Override
	protected BeanDefinition buildBeanDefinition(
			List<AnnotationConfigurer<SpringDataStoreWriterConfigs, SpringDatasetWriterBuilder>> configurers) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("onConfigurers: " + configurers);
		}

		for (AnnotationConfigurer<SpringDataStoreWriterConfigs, SpringDatasetWriterBuilder> configurer : configurers) {
			builder.apply(configurer);
		}
		return builder.getOrBuild().getWriter();
	}

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableDatasetWriter.class;
	}

}
