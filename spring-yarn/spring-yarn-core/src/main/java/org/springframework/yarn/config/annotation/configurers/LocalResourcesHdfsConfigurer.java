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
package org.springframework.yarn.config.annotation.configurers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.config.annotation.builders.YarnResourceLocalizerBuilder;
import org.springframework.yarn.fs.LocalResourcesFactoryBean.TransferEntry;
import org.springframework.yarn.fs.ResourceLocalizer;

public class LocalResourcesHdfsConfigurer extends AnnotationConfigurerAdapter<ResourceLocalizer, YarnResourceLocalizerBuilder> {

	private Collection<TransferEntry> hdfsEntries = new ArrayList<TransferEntry>();

	@Override
	public void configure(YarnResourceLocalizerBuilder builder) throws Exception {
		builder.setHdfsEntries(hdfsEntries);
	}

	public LocalResourcesHdfsConfigurer hdfs(String path) {
		hdfsEntries.add(new TransferEntry(null, null, path, null, null, false));
		return this;
	}

}
