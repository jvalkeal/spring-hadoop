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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.data.hadoop.store.PartitionDataStoreWriter;
import org.springframework.data.hadoop.store.TestUtils;
import org.springframework.data.hadoop.store.config.annotation.builders.DataStoreWriterConfigurer;
import org.springframework.data.hadoop.store.output.PartitionTextFileWriter;
import org.springframework.data.hadoop.store.partition.PartitionKeyResolver;
import org.springframework.data.hadoop.store.partition.PartitionResolver;
import org.springframework.data.hadoop.store.partition.PartitionStrategy;

public class SpringWriterConfigurationTests {

	private final static String WRITER1_ID = "dataStoreWriter1";
	private final static String WRITER1_ID_ALIAS = "dataStoreWriter1Alias";
	private final static String WRITER2_ID = "dataStoreWriter2";
	private final static String WRITER5_ID = "dataStoreWriter5";

	@Test
	public void testBaseConfigWithHadoopConfiguration() throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config1.class,
				ConfigurationConfig.class);
		@SuppressWarnings("rawtypes")
		DataStoreWriter writer1 = ctx.getBean(WRITER1_ID, DataStoreWriter.class);
		assertNotNull(writer1);
		org.apache.hadoop.conf.Configuration configuration = TestUtils.callMethod("getConfiguration", writer1);
		assertNotNull(configuration);
		@SuppressWarnings("rawtypes")
		DataStoreWriter writer1a = ctx.getBean(WRITER1_ID_ALIAS, DataStoreWriter.class);
		assertNotNull(writer1a);
		assertThat(writer1, sameInstance(writer1));
		ctx.close();
	}

	@Test
	public void testAutoGeneratedWriterNames() throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config3.class, Config4.class,
				ConfigurationConfig.class);
		@SuppressWarnings("rawtypes")
		Map<String, DataStoreWriter> writers = ctx.getBeansOfType(DataStoreWriter.class);
		assertThat(writers.size(), is(2));
		ctx.close();
	}

	@Test
	public void testGivenWriterNames() throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config1.class, Config2.class,
				ConfigurationConfig.class);
		DataStoreWriter<?> writer1 = ctx.getBean(WRITER1_ID, DataStoreWriter.class);
		assertNotNull(writer1);
		org.apache.hadoop.conf.Configuration configuration = TestUtils.callMethod("getConfiguration", writer1);
		assertNotNull(configuration);
		DataStoreWriter<?> writer2 = ctx.getBean(WRITER2_ID, DataStoreWriter.class);
		assertNotNull(writer2);
		ctx.close();
	}

	@Test
	public void testAutowireType1() throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config3.class,
				ConfigurationConfig.class, AutowireTypeBean1.class);
		AutowireTypeBean1 bean = ctx.getBean(AutowireTypeBean1.class);
		assertNotNull(bean.writer);
		ctx.close();
	}

	@Test
	public void testAutowireType2() throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config5.class,
				ConfigurationConfig.class, AutowireTypeBean2.class);
		AutowireTypeBean2 bean = ctx.getBean(AutowireTypeBean2.class);
		assertNotNull(bean.writer);
		ctx.close();
	}

	@Test
	public void testPartitionWriter() throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config5.class,
				ConfigurationConfig.class);
		Object writer = ctx.getBean(WRITER5_ID);
		assertNotNull(writer);
		assertThat(writer, instanceOf(PartitionTextFileWriter.class));
		ctx.close();
	}

	@Configuration
	@EnableDataStoreWriter(name={WRITER1_ID, WRITER1_ID_ALIAS})
	static class Config1 extends SpringDataStoreWriterConfigurerAdapter {

		@Override
		public void configure(DataStoreWriterConfigurer config) throws Exception {
			config
				.basePath("/tmp/foo1");
		}

	}

	@Configuration
	@EnableDataStoreWriter(name=WRITER2_ID)
	static class Config2 extends SpringDataStoreWriterConfigurerAdapter {

		@Override
		public void configure(DataStoreWriterConfigurer config) throws Exception {
			config
				.basePath("/tmp/foo2");
		}

	}

	@Configuration
	@EnableDataStoreWriter
	static class Config3 extends SpringDataStoreWriterConfigurerAdapter {

		@Override
		public void configure(DataStoreWriterConfigurer config) throws Exception {
			config
				.basePath("/tmp/foo3");
		}

	}

	@Configuration
	@EnableDataStoreWriter
	static class Config4 extends SpringDataStoreWriterConfigurerAdapter {

		@Override
		public void configure(DataStoreWriterConfigurer config) throws Exception {
			config
				.basePath("/tmp/foo4");
		}

	}

	@Configuration
	@EnableDataStoreWriter(name=WRITER5_ID)
	static class Config5 extends SpringDataStoreWriterConfigurerAdapter {

		@Override
		public void configure(DataStoreWriterConfigurer config) throws Exception {
			config
				.basePath("/tmp/foo4")
				.withPartitionStrategy()
					.custom(partitionStrategy());
		}

		@Bean
		public PartitionStrategy<String, String> partitionStrategy() {
			return new PartitionStrategy<String, String>() {

				@Override
				public PartitionResolver<String> getPartitionResolver() {
					return null;
				}

				@Override
				public PartitionKeyResolver<String, String> getPartitionKeyResolver() {
					return null;
				}
			};
		}

	}

	/**
	 * TextFileWriter
	 * PartitionTextFileWriter
	 * 		- only when we have partition expression???
	 * DelimitedTextFileWriter
	 * TextSequenceFileWriter
	 *
	 * FileNamingStrategy
	 *
	 * RolloverStrategy
	 *
	 */
	@Configuration
	@EnableDataStoreWriter
	static class FooRemoveMe extends SpringDataStoreWriterConfigurerAdapter {

		@Override
		public void configure(DataStoreWriterConfigurer config) throws Exception {
			config
				.configuration(null)
				.basePath("/tmp/foo1")
				.codec("gzip")
				.overwrite(true)
				.idleTimeout(60000)
				.fileOpenAttempts(10)
				.withPartitionStrategy()
					.custom(partitionStrategy())
					.map("myspel")
					.and()
				.withNamingStrategy()
					.name("foo")
					.uuid()
					.rolling()
					.codec()
					.and()
				.withRolloverStrategy()
					.size(1000)
					.size("1M");

		}

		@Bean
		public PartitionStrategy<String, String> partitionStrategy() {
			return new PartitionStrategy<String, String>() {

				@Override
				public PartitionResolver<String> getPartitionResolver() {
					return null;
				}

				@Override
				public PartitionKeyResolver<String, String> getPartitionKeyResolver() {
					return null;
				}
			};
		}

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

	static class AutowireTypeBean1 {

		@Autowired
		DataStoreWriter<String> writer;
	}

	static class AutowireTypeBean2 {

		@Autowired
		PartitionDataStoreWriter<String, Map<String, Object>> writer;
	}

}
