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

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.client.YarnClient;
import org.springframework.yarn.client.YarnClientFactoryBean;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;

public class YarnClientBuilder extends AbstractConfiguredAnnotationBuilder<YarnClient, YarnClientBuilder> {

	private YarnConfiguration configuration;
	private String appName;

	public YarnClientBuilder() {
		this(true);
	}

	public YarnClientBuilder(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	@Override
	protected YarnClient performBuild() throws Exception {
		YarnClientFactoryBean fb = new YarnClientFactoryBean();
		fb.setConfiguration(configuration);
		fb.setAppName(appName);
		fb.afterPropertiesSet();
		return fb.getObject();
	}

	public void configuration(YarnConfiguration configuration) {
		this.configuration = configuration;
	}

	public YarnClientBuilder appName(String appName) {
		this.appName = appName;
		return this;
	}

}
