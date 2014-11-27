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
package org.springframework.data.hadoop.store.config.annotation;

import org.springframework.data.hadoop.config.common.annotation.AnnotationConfigurer;
import org.springframework.data.hadoop.store.config.annotation.builders.DatasetDefinitionConfigurer;
import org.springframework.data.hadoop.store.config.annotation.builders.DatasetRepositoryFactoryConfigurer;
import org.springframework.data.hadoop.store.config.annotation.builders.DatasetWriterConfigurer;
import org.springframework.data.hadoop.store.config.annotation.builders.SpringDatasetWriterBuilder;

public interface SpringDatasetWriterConfigurer extends AnnotationConfigurer<SpringDataStoreWriterConfigs, SpringDatasetWriterBuilder> {

	void configure(DatasetWriterConfigurer writer) throws Exception;

	void configure(DatasetRepositoryFactoryConfigurer factory) throws Exception;

	void configure(DatasetDefinitionConfigurer definition) throws Exception;

}
