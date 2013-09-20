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

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.data.config.annotation.configurers.PropertiesConfigureAware;
import org.springframework.data.config.annotation.configurers.PropertiesConfigurer;
import org.springframework.yarn.config.annotation.SpringYarnConfigurerAdapter;
import org.springframework.yarn.config.annotation.configurers.EnvironmentClasspathConfigurer;
import org.springframework.yarn.configuration.EnvironmentFactoryBean;

/**
 * {@link AnnotationBuilder} for Yarn environment.
 *
 * @author Janne Valkealahti
 *
 */
public final class YarnEnvironmentBuilder
		extends AbstractConfiguredAnnotationBuilder<Map<String, String>, YarnEnvironmentBuilder>
		implements PropertiesConfigureAware {

	private String classpath;
	private boolean defaultClasspath = true;
	private boolean includeSystemEnv;
	private String delimiter;
	private Properties properties = new Properties();

	public YarnEnvironmentBuilder() {
	}

	@Override
	protected Map<String, String> performBuild() throws Exception {
		EnvironmentFactoryBean fb = new EnvironmentFactoryBean();
		fb.setProperties(properties);
		fb.setClasspath(classpath);
		fb.setDelimiter(":");
		fb.setDefaultYarnAppClasspath(defaultClasspath);
		fb.setIncludeSystemEnv(includeSystemEnv);
		fb.afterPropertiesSet();
		return fb.getObject();
	}

	/**
	 * Specify a classpath environment variable.
	 * <p>
	 * Applies a new {@link EnvironmentClasspathConfigurer} into current
	 * builder. Equivalents between JavaConfig and XML are shown below.
	 *
	 * <p>JavaConfig:
	 * <pre>
	 * &#064;Configuration
	 * &#064;EnableYarn
	 * static class Config extends SpringYarnConfigurerAdapter {
	 *
	 *   &#064;Override
	 *   public void configure(YarnEnvironmentBuilder environment) throws Exception {
	 *     environment
	 *       .withClasspath()
	 *         .entry("cpEntry1")
	 *         .defaultYarnAppClasspath(true)
	 *         .delimiter(":")
	 *   }
	 *
	 * }
	 * </pre>
	 * <p>XML:
	 * <pre>
	 * &lt;yarn:environment>
	 *   &lt;yarn:classpath default-yarn-app-classpath="true" delimiter=":">
	 *     cpEntry1
	 *   &lt;/yarn:classpath>
	 * &lt;/yarn:environment>
	 * </pre>
	 *
	 * @return {@link EnvironmentClasspathConfigurer} for classpath
	 * @throws Exception if error occurred
	 */
	public EnvironmentClasspathConfigurer withClasspath() throws Exception {
		return apply(new EnvironmentClasspathConfigurer());
	}

	public PropertiesConfigurer<Map<String, String>, YarnEnvironmentBuilder> withProperties() throws Exception {
		return apply(new PropertiesConfigurer<Map<String, String>, YarnEnvironmentBuilder>());
	}

	public YarnEnvironmentBuilder entry(String key, String value) {
		properties.put(key, value);
		return this;
	}

	public YarnEnvironmentBuilder propertiesLocation(String... locations) throws IOException {
		for (String location : locations) {
			PropertiesFactoryBean fb = new PropertiesFactoryBean();
			fb.setLocation(new ClassPathResource(location));
			fb.afterPropertiesSet();
			properties.putAll(fb.getObject());
		}
		return this;
	}

	public YarnEnvironmentBuilder includeSystemEnv(boolean includeSystemEnv) {
		this.includeSystemEnv = includeSystemEnv;
		return this;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public void setDefaultClasspath(boolean defaultClasspath) {
		this.defaultClasspath = defaultClasspath;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public void configureProperties(Properties properties) {
		this.properties.putAll(properties);
	}

}
