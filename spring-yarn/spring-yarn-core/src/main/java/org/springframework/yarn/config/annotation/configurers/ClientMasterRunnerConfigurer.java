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

import org.springframework.data.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.CommandLineAppmasterRunner;
import org.springframework.yarn.client.YarnClient;
import org.springframework.yarn.config.annotation.builders.YarnClientBuilder;
import org.springframework.yarn.launch.LaunchCommandsFactoryBean;

public class ClientMasterRunnerConfigurer extends AnnotationConfigurerAdapter<YarnClient, YarnClientBuilder> {

	private Class<?> clazz;

	@Override
	public void configure(YarnClientBuilder builder) throws Exception {
		LaunchCommandsFactoryBean fb = new LaunchCommandsFactoryBean();
		fb.setRunner(CommandLineAppmasterRunner.class);
		fb.setContextFile(clazz.getCanonicalName());
		fb.setBeanName(YarnSystemConstants.DEFAULT_ID_APPMASTER);
		fb.setStdout("<LOG_DIR>/Appmaster.stdout");
		fb.setStderr("<LOG_DIR>/Appmaster.stderr");
		fb.afterPropertiesSet();
		builder.setCommands(fb.getObject());
	}

	public ClientMasterRunnerConfigurer clazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

}
