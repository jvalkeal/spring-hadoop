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

import java.util.Properties;

import org.springframework.data.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.CommandLineAppmasterRunner;
import org.springframework.yarn.client.YarnClient;
import org.springframework.yarn.config.annotation.builders.YarnClientBuilder;
import org.springframework.yarn.config.annotation.builders.YarnClientConfigure;
import org.springframework.yarn.launch.LaunchCommandsFactoryBean;

/**
 * {@link AnnotationConfigurer} for {@link YarnAppmaster} launch commands.
 *
 * @author Janne Valkealahti
 *
 */
public class ClientMasterRunnerConfigurer
		extends AnnotationConfigurerAdapter<YarnClient, YarnClientConfigure, YarnClientBuilder>
		implements ClientMasterRunnerConfigure {

	/** Spring context configuration class */
	private Class<?> contextClazz;

	/** Spring context configuration file */
	private String contextFile;

	/** Bean name in launcher */
	private String beanName = YarnSystemConstants.DEFAULT_ID_APPMASTER;

	/** Stdout path */
	private String stdout = "<LOG_DIR>/Appmaster.stdout";

	/** Stderr path */
	private String stderr = "<LOG_DIR>/Appmaster.stderr";

	/** Command line arguments */
	private Properties arguments;

	@Override
	public void configure(YarnClientBuilder builder) throws Exception {
		LaunchCommandsFactoryBean fb = new LaunchCommandsFactoryBean();
		fb.setRunner(CommandLineAppmasterRunner.class);
		fb.setContextFile(determineContextConfig());
		fb.setBeanName(beanName);
		fb.setStdout(stdout);
		fb.setStderr(stderr);
		fb.setArguments(arguments);
		fb.afterPropertiesSet();
		builder.setCommands(fb.getObject());
	}

	@Override
	public ClientMasterRunnerConfigure contextClass(Class<?> clazz) {
		contextClazz = clazz;
		return this;
	}

	@Override
	public ClientMasterRunnerConfigure contextFile(String file) {
		contextFile = file;
		return this;
	}

	@Override
	public ClientMasterRunnerConfigure beanName(String bean) {
		beanName = bean;
		return this;
	}

	@Override
	public ClientMasterRunnerConfigure stdout(String path) {
		stdout = path;
		return this;
	}

	@Override
	public ClientMasterRunnerConfigure stderr(String path) {
		stderr = path;
		return this;
	}

	@Override
	public ClientMasterRunnerConfigure arguments(Properties arguments) {
		this.arguments = arguments;
		return this;
	}

	private String determineContextConfig() {
		if (contextFile != null) {
			return contextFile;
		} else if (contextClazz != null) {
			return contextClazz.getCanonicalName();
		} else {
			return YarnSystemConstants.DEFAULT_CONTEXT_FILE_APPMASTER;
		}
	}

}
