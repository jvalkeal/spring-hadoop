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
import java.util.Map;

import org.springframework.data.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.util.StringUtils;
import org.springframework.yarn.config.annotation.builders.YarnEnvironmentBuilder;
import org.springframework.yarn.config.annotation.builders.YarnEnvironmentConfigure;

/**
 * {@link org.springframework.data.config.annotation.AnnotationConfigurer AnnotationConfigurer}
 * which knows how to handle configuring a classpath.
 *
 * @author Janne Valkealahti
 *
 */
public class EnvironmentClasspathConfigurer extends AnnotationConfigurerAdapter<Map<String, String>, YarnEnvironmentConfigure, YarnEnvironmentBuilder> {

	private Boolean defaultClasspath;
	private String delimiter;

	private ArrayList<String> entries = new ArrayList<String>();

	@Override
	public void configure(YarnEnvironmentBuilder builder) throws Exception {
		builder.setClasspath(StringUtils.collectionToDelimitedString(entries, ":"));
	}

	public EnvironmentClasspathConfigurer entry(String entry) {
		entries.add(entry);
		return this;
	}

	public EnvironmentClasspathConfigurer defaultYarnAppClasspath(boolean defaultClasspath) {
		this.defaultClasspath = defaultClasspath;
		return this;
	}

	public EnvironmentClasspathConfigurer delimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

}
