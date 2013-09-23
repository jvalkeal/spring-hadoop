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

import org.springframework.data.config.annotation.AnnotationConfigurerBuilder;
import org.springframework.data.config.annotation.configurers.PropertiesConfigurer;
import org.springframework.yarn.config.annotation.builders.YarnAppmasterConfigure;
import org.springframework.yarn.config.annotation.configurers.MasterContainerRunnerConfigurer.ArgumentsBuilder;
import org.springframework.yarn.launch.AbstractCommandLineRunner;

public interface MasterContainerRunnerConfigure extends AnnotationConfigurerBuilder<YarnAppmasterConfigure> {

	MasterContainerRunnerConfigure contextClass(Class<?> contextClass);

	MasterContainerRunnerConfigure contextFile(String contextFile);

	MasterContainerRunnerConfigure stdout(String stdout);

	MasterContainerRunnerConfigure stderr(String stderr);

	MasterContainerRunnerConfigure beanName(String beanName);

	MasterContainerRunnerConfigure runnerClass(Class<? extends AbstractCommandLineRunner<?>> runnerClazz);

	MasterContainerRunnerConfigure arguments(Properties arguments);

	PropertiesConfigurer<Properties, ArgumentsBuilder, ArgumentsBuilder> withArguments() throws Exception;
}
