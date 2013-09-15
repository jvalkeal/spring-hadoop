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
package org.springframework.yarn.config.annotation.builders;

import java.util.Map;

import org.springframework.data.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.configurers.EnvironmentClasspathConfigurer;
import org.springframework.yarn.configuration.EnvironmentFactoryBean;

public final class YarnEnvironmentBuilder
		extends AbstractConfiguredAnnotationBuilder<Map<String, String>, YarnEnvironmentBuilder> {

	private String classpath;

	public YarnEnvironmentBuilder() {
//		this(true);
	}

//	public YarnEnvironmentBuilder(boolean allowConfigurersOfSameType) {
//		super(allowConfigurersOfSameType);
//	}

	@Override
	protected Map<String, String> performBuild() throws Exception {
		EnvironmentFactoryBean fb = new EnvironmentFactoryBean();
		fb.setClasspath(classpath);
		fb.setDelimiter(":");
		fb.setDefaultYarnAppClasspath(true);
		fb.afterPropertiesSet();
		return fb.getObject();
	}

	public EnvironmentClasspathConfigurer withClasspath() throws Exception {
		return apply(new EnvironmentClasspathConfigurer());
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

}
