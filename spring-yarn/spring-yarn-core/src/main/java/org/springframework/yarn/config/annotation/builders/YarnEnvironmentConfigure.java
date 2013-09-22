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

import org.springframework.data.config.annotation.configurers.PropertiesConfigure;
import org.springframework.yarn.config.annotation.SpringYarnConfigurerAdapter;
import org.springframework.yarn.config.annotation.configurers.EnvironmentClasspathConfigurer;

/**
 * Interface for {@link YarnEnvironmentBuilder} used from
 * a {@link SpringYarnConfigurerAdapter}.
 *
 * @author Janne Valkealahti
 *
 */
public interface YarnEnvironmentConfigure {

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
	 *         .entry("cpEntry2")
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
	 *     cpEntry2
	 *   &lt;/yarn:classpath>
	 * &lt;/yarn:environment>
	 * </pre>
	 *
	 * @return {@link EnvironmentClasspathConfigurer} for classpath
	 * @throws Exception if error occurred
	 */
	EnvironmentClasspathConfigurer withClasspath() throws Exception;

	YarnEnvironmentConfigure entry(String key, String value);

	YarnEnvironmentConfigure propertiesLocation(String... locations) throws IOException;

	YarnEnvironmentConfigure includeSystemEnv(boolean includeSystemEnv);

	PropertiesConfigure<YarnEnvironmentConfigure> withProperties() throws Exception;

}
