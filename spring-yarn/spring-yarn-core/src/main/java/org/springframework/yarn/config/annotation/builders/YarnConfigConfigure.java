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

import org.springframework.data.config.annotation.configurers.PropertiesConfigure;
import org.springframework.data.config.annotation.configurers.ResourceConfigure;
import org.springframework.yarn.config.annotation.SpringYarnConfigurerAdapter;

/**
 * Interface for {@link YarnConfigBuilder} used from
 * a {@link SpringYarnConfigurerAdapter}.
 * <p>
 * Typically configuration is used as shown below.
 * <p>
 * <pre>
 * &#064;Configuration
 * &#064;EnableYarn
 * static class Config extends SpringYarnConfigurerAdapter {
 *
 *   &#064;Override
 *   public void configure(YarnConfigConfigure config) throws Exception {
 *     config
 *       .fileSystemUri("hdfs://foo.uri")
 *       .withResources()
 *         .resource("classpath:/test-site-1.xml")
 *         .resource("classpath:/test-site-2.xml")
 *         .and()
 *       .withProperties()
 *         .property("foo", "jee");
 *   }
 *
 * }
 * </pre>
 *
 * @author Janne Valkealahti
 *
 */
public interface YarnConfigConfigure {

	// TODO: add javadoc
	/**
	 *
	 * @return {@link ResourceConfigure} for chaining
	 * @throws Exception if error occurred
	 */
	ResourceConfigure<YarnConfigConfigure> withResources() throws Exception;

	// TODO: add javadoc
	/**
	 *
	 * @return {@link PropertiesConfigure} for chaining
	 * @throws Exception if error occurred
	 */
	PropertiesConfigure<YarnConfigConfigure> withProperties() throws Exception;

	// TODO: add xml example after xml is supported
	/**
	 * Specify a Hdfs file system uri.
	 *
	 * <p>JavaConfig:
	 * <pre>
	 * public void configure(YarnConfigConfigure config) throws Exception {
	 *   config
	 *     .fileSystemUri("hdfs://myhost:1234");
	 * }
	 * </pre>
	 *
	 * <p>XML:
	 * <pre>
	 * &lt;yarn:environment/>
	 * </pre>
	 *
	 * @param uri The Hdfs uri
	 * @return {@link YarnConfigConfigure} for chaining
	 */
	YarnConfigConfigure fileSystemUri(String uri);

	// TODO: add xml example after xml is supported
	/**
	 * Specify a Yarn resource manager address.
	 *
	 * <p>JavaConfig:
	 * <pre>
	 * public void configure(YarnConfigConfigure config) throws Exception {
	 *   config
	 *     .resourceManagerAddress("myRmHost:1234");
	 * }
	 * </pre>
	 *
	 * <p>XML:
	 * <pre>
	 * &lt;yarn:environment/>
	 * </pre>
	 *
	 * @param address The Yarn resource manager address
	 * @return {@link YarnConfigConfigure} for chaining
	 */
	YarnConfigConfigure resourceManagerAddress(String address);

}
