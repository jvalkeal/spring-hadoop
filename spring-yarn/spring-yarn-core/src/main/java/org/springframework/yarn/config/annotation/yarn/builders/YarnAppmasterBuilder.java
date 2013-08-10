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
package org.springframework.yarn.config.annotation.yarn.builders;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.springframework.yarn.am.StaticAppmaster;
import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.am.allocate.DefaultContainerAllocator;
import org.springframework.yarn.am.container.DefaultContainerLauncher;
import org.springframework.yarn.am.monitor.DefaultContainerMonitor;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.ObjectPostProcessor;
import org.springframework.yarn.config.annotation.yarn.configurers.MasterContainerRunnerConfigurer;
import org.springframework.yarn.fs.ResourceLocalizer;

public final class YarnAppmasterBuilder extends AbstractConfiguredAnnotationBuilder<YarnAppmaster, YarnAppmasterBuilder> {

//	private Class<?> clazz;
	private Configuration configuration;
	private ResourceLocalizer resourceLocalizer;
	private Map<String, String> environment;
	private String[] commands;

	public YarnAppmasterBuilder() {
	}

	public YarnAppmasterBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
		super(objectPostProcessor);
	}

	@Override
	protected YarnAppmaster performBuild() throws Exception {
		StaticAppmaster am = new StaticAppmaster();

		if (commands != null) {
			am.setCommands(commands);
		}

		am.setConfiguration(configuration);
		am.setEnvironment(environment);
		am.setResourceLocalizer(resourceLocalizer);

		DefaultContainerLauncher launcher = new DefaultContainerLauncher();
		launcher.setConfiguration(configuration);
		launcher.setEnvironment(environment);
		launcher.setResourceLocalizer(resourceLocalizer);
		am.setLauncher(launcher);

		DefaultContainerAllocator allocator = new DefaultContainerAllocator();
		allocator.setConfiguration(configuration);
		allocator.setEnvironment(environment);
		allocator = postProcess(allocator);
		am.setAllocator(allocator);

		am.setMonitor(new DefaultContainerMonitor());

		return am;
	}

	public void configuration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
		this.resourceLocalizer = resourceLocalizer;
	}

	public MasterContainerRunnerConfigurer withContainerRunner() throws Exception {
		return apply(new MasterContainerRunnerConfigurer());
	}

	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}

//	public YarnAppmasterBuilder clazz(Class<?> clazz) {
//		this.clazz = clazz;
//		return this;
//	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

}
