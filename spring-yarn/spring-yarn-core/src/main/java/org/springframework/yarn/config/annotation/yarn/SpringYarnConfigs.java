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
package org.springframework.yarn.config.annotation.yarn;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * A holder object for all configured configs for Spring Yarn.
 *
 * @author Janne Valkealahti
 *
 */
public class SpringYarnConfigs {

	private Configuration configuration;

	private ResourceLocalizer localizer;

	private Map<String, String> environment;

	public SpringYarnConfigs() {}

	public SpringYarnConfigs(Configuration configuration, ResourceLocalizer localizer, Map<String, String> environment) {
		super();
		this.configuration = configuration;
		this.localizer = localizer;
		this.environment = environment;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public ResourceLocalizer getLocalizer() {
		return localizer;
	}

	public void setLocalizer(ResourceLocalizer localizer) {
		this.localizer = localizer;
	}

	public Map<String, String> getEnvironment() {
		return environment;
	}

	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}

}
