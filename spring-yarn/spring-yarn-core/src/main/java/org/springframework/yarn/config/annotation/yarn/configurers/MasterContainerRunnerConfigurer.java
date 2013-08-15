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
package org.springframework.yarn.config.annotation.yarn.configurers;

import java.util.Properties;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.config.annotation.configurers.PropertiesConfigureAware;
import org.springframework.yarn.config.annotation.configurers.PropertiesConfigurer;
import org.springframework.yarn.config.annotation.yarn.builders.YarnAppmasterBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfigBuilder;
import org.springframework.yarn.container.CommandLineContainerRunner;
import org.springframework.yarn.launch.AbstractCommandLineRunner;
import org.springframework.yarn.launch.LaunchCommandsFactoryBean;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public class MasterContainerRunnerConfigurer extends AnnotationConfigurerAdapter<YarnAppmaster, YarnAppmasterBuilder> {

	private Class<?> contextClass;
	private String contextFile = "container-context.xml";
	private String stdout = "<LOG_DIR>/Container.stdout";
	private String stderr = "<LOG_DIR>/Container.stderr";
	private String beanName = YarnSystemConstants.DEFAULT_ID_CONTAINER;
	private Class<? extends AbstractCommandLineRunner<?>> runnerClazz = CommandLineContainerRunner.class;

	private Properties arguments = new Properties();
	private ArgumentsBuilder argumentsBuilder;

	@Override
	public void configure(YarnAppmasterBuilder builder) throws Exception {
		LaunchCommandsFactoryBean fb = new LaunchCommandsFactoryBean();
		fb.setRunner(runnerClazz);
		fb.setContextFile(contextClass != null ?  contextClass.getCanonicalName() : contextFile);
		fb.setBeanName(beanName);

		if (argumentsBuilder != null) {
			arguments.putAll(argumentsBuilder.build());
		}
		fb.setArguments(arguments);

		fb.setStdout(stdout);
		fb.setStderr(stderr);
		fb.afterPropertiesSet();
		builder.setCommands(fb.getObject());
	}

	@Override
	public void init(YarnAppmasterBuilder builder) throws Exception {
		super.init(builder);
	}

	public MasterContainerRunnerConfigurer contextClass(Class<?> contextClass) {
		this.contextClass = contextClass;
		return this;
	}

	public MasterContainerRunnerConfigurer contextFile(String contextFile) {
		this.contextFile = contextFile;
		return this;
	}

	public MasterContainerRunnerConfigurer stdout(String stdout) {
		this.stdout = stdout;
		return this;
	}

	public MasterContainerRunnerConfigurer stderr(String stderr) {
		this.stderr = stderr;
		return this;
	}

	public MasterContainerRunnerConfigurer beanName(String beanName) {
		this.beanName = beanName;
		return this;
	}

	public MasterContainerRunnerConfigurer runnerClass(Class<? extends AbstractCommandLineRunner<?>> runnerClazz) {
		this.runnerClazz = runnerClazz;
		return this;
	}

	public MasterContainerRunnerConfigurer arguments(Properties arguments) {
		this.arguments = arguments;
		return this;
	}

	public PropertiesConfigurer<Properties, ArgumentsBuilder> withArguments() throws Exception {
		if (argumentsBuilder == null) {
			argumentsBuilder = new ArgumentsBuilder(new PropertiesConfigurer<Properties, ArgumentsBuilder>());
		}
		return argumentsBuilder.propertiesConfigurer;
	}


	public class ArgumentsBuilder implements AnnotationBuilder<Properties>, PropertiesConfigureAware {

		private PropertiesConfigurer<Properties, ArgumentsBuilder> propertiesConfigurer;
		private Properties properties;

		public ArgumentsBuilder(PropertiesConfigurer<Properties, ArgumentsBuilder> propertiesConfigurer) {
			this.propertiesConfigurer = propertiesConfigurer;
			this.propertiesConfigurer.setBuilder(this);
		}

		@Override
		public Properties build() throws Exception {
			propertiesConfigurer.configure(argumentsBuilder);
			return properties;
		}

		@Override
		public void configureProperties(Properties properties) {
			this.properties = properties;
		}

		public MasterContainerRunnerConfigurer and() {
			return MasterContainerRunnerConfigurer.this;
		}

	}



}
