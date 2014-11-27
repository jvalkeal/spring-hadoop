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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.data.hadoop.store.config.annotation.builders.DatasetDefinitionConfigurer;
import org.springframework.data.hadoop.store.config.annotation.builders.DatasetRepositoryFactoryConfigurer;
import org.springframework.data.hadoop.store.config.annotation.builders.DatasetWriterConfigurer;

public class SpringDatasetWriterConfigurationTests {

	@Test
	public void testAutoGeneratedWriterNames() throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(FooRemoveThis.class);
		@SuppressWarnings("rawtypes")
		Map<String, DataStoreWriter> writers = ctx.getBeansOfType(DataStoreWriter.class);
		assertThat(writers.size(), is(2));
		ctx.close();
	}

	@Configuration
	@EnableHadoop
	static class ConfigurationConfig extends SpringHadoopConfigurerAdapter {

		@Override
		public void configure(HadoopConfigConfigurer config) throws Exception {
			config
				.fileSystemUri("hdfs://localhost:8021");
		}

	}

	@Configuration
	@EnableDatasetWriter
	static class FooRemoveThis extends SpringDatasetWriterConfigurerAdapter {

		@Override
		public void configure(DatasetWriterConfigurer writer) throws Exception {
			writer
				.entityClass(Object.class);
		}

		@Override
		public void configure(DatasetRepositoryFactoryConfigurer factory) throws Exception {
			factory
				.configuration(null)
				.basePath("/tmp/foo")
				.namespace("test");
		}

		@Override
		public void configure(DatasetDefinitionConfigurer definition) throws Exception {
			definition
				.targetClass(Object.class)
				.allowNullValues(false)
				.format("avro")
				.writerCacheSize(1000)
				.compressionType("bzip2")
				.partitionStrategy(null);
		}


	}

}
