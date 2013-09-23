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

import org.springframework.yarn.config.annotation.configurers.ClientMasterRunnerConfigure;
import org.springframework.yarn.config.annotation.configurers.ClientMasterRunnerConfigurer;

/**
 * Interface for {@link YarnClientBuilder}. Typical use case
 * and difference between xml and java are shown below.
 * <p>
 *
 *
 * <p>JavaConfig:
 * <pre>
 * &#064;Configuration
 * &#064;EnableYarn
 * static class Config extends SpringYarnConfigurerAdapter {
 *
 *   &#064;Override
 *   public void configure(YarnClientConfigure client) throws Exception {
 *     client
 *       .withMasterRunner()
 *         .contextClass(MyAppmasterConfiguration.class);
 *   }
 *
 * }
 * </pre>
 * <p>XML:
 * <pre>
 * &lt;yarn:client>
 *   &lt;yarn:master-runner />
 * &lt;/yarn:client>
 * </pre>
 *
 * @author Janne Valkealahti
 *
 */
public interface YarnClientConfigure {

	/**
	 * Specify a Appmaster runner. Applies a new {@link ClientMasterRunnerConfigurer}
	 * into current builder.
	 *
	 * @return
	 * @throws Exception
	 */
	ClientMasterRunnerConfigure withMasterRunner() throws Exception;

	YarnClientConfigure appName(String appName);

}
