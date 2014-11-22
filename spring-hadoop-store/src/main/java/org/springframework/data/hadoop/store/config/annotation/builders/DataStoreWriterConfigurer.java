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
package org.springframework.data.hadoop.store.config.annotation.builders;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.springframework.data.hadoop.store.codec.CodecInfo;
import org.springframework.data.hadoop.store.codec.Codecs;
import org.springframework.data.hadoop.store.config.annotation.configurers.NamingStrategyConfigurer;
import org.springframework.data.hadoop.store.config.annotation.configurers.PartitionStrategyConfigurer;
import org.springframework.data.hadoop.store.config.annotation.configurers.RolloverStrategyConfigurer;

public interface DataStoreWriterConfigurer {

	DataStoreWriterConfigurer configuration(Configuration configuration);

	DataStoreWriterConfigurer basePath(Path path);

	DataStoreWriterConfigurer basePath(String path);

	DataStoreWriterConfigurer codec(CodecInfo codec);

	DataStoreWriterConfigurer codec(String codec);

	DataStoreWriterConfigurer codec(Codecs codec);

	DataStoreWriterConfigurer overwrite(boolean overwrite);

	DataStoreWriterConfigurer inWritingPrefix(String prefix);

	DataStoreWriterConfigurer inWritingSuffix(String suffix);

	DataStoreWriterConfigurer idleTimeout(long timeout);

	DataStoreWriterConfigurer fileOpenAttempts(int attempts);

	PartitionStrategyConfigurer withPartitionStrategy() throws Exception;

	NamingStrategyConfigurer withNamingStrategy() throws Exception;

	RolloverStrategyConfigurer withRolloverStrategy() throws Exception;

}
