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

import org.springframework.data.config.annotation.AnnotationConfigurer;
import org.springframework.data.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.config.annotation.builders.YarnResourceLocalizerBuilder;
import org.springframework.yarn.fs.LocalResourcesFactoryBean.CopyEntry;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * {@link AnnotationConfigurer} which knows how to handle
 * copy entries in {@link ResourceLocalizer}.
 *
 * @author Janne Valkealahti
 *
 */
public class LocalResourcesCopyConfigurer extends AnnotationConfigurerAdapter<ResourceLocalizer, YarnResourceLocalizerBuilder, YarnResourceLocalizerBuilder> {

	private Collection<CopyEntry> copyEntries = new ArrayList<CopyEntry>();

	@Override
	public void configure(YarnResourceLocalizerBuilder builder) throws Exception {
		builder.setCopyEntries(copyEntries);
	}

	public LocalResourcesCopyConfigurer copy(String src, String dest, boolean staging) {
		copyEntries.add(new CopyEntry(src, dest, staging));
		return this;
	}

	public ConfiguredCopyEntry source(String source) {
		return new ConfiguredCopyEntry(source);
	}

	public final class ConfiguredCopyEntry {
		private String source;
		private String destination;
		private ConfiguredCopyEntry(String source) {
			this.source = source;
		}
		public ConfiguredCopyEntry destination(String destination) {
			this.destination = destination;
			return this;
		}
		public LocalResourcesCopyConfigurer staging(boolean staging) {
			copyEntries.add(new CopyEntry(source, destination, staging));
			return LocalResourcesCopyConfigurer.this;
		}
	}

}
