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
package org.springframework.data.hadoop.store.config.annotation.configurers;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.data.hadoop.config.common.annotation.AnnotationConfigurerAdapter;
import org.springframework.data.hadoop.store.config.annotation.builders.DataStoreWriterBuilder;
import org.springframework.data.hadoop.store.config.annotation.builders.DataStoreWriterConfigurer;
import org.springframework.data.hadoop.store.partition.DefaultPartitionStrategy;
import org.springframework.data.hadoop.store.partition.PartitionStrategy;

public class DefaultPartitionStrategyConfigurer extends AnnotationConfigurerAdapter<BeanDefinition, DataStoreWriterConfigurer, DataStoreWriterBuilder> implements PartitionStrategyConfigurer {

	private PartitionStrategy<?, ?> partitionStrategy;

	@Override
	public void configure(DataStoreWriterBuilder builder) throws Exception {
		if (partitionStrategy != null) {
			builder.setPartitionStrategy(partitionStrategy);
		}
	}

	@Override
	public PartitionStrategyConfigurer custom(PartitionStrategy<?, ?> partitionStrategy) {
		this.partitionStrategy = partitionStrategy;
		return this;
	}

	@Override
	public PartitionStrategyConfigurer map(String expression) {
		this.partitionStrategy = new DefaultPartitionStrategy<Object>(expression);
		return this;
	}

}
