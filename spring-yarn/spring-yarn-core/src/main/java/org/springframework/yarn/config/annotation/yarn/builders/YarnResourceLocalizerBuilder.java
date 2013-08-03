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

import java.util.ArrayList;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.fs.LocalResourcesFactoryBean;
import org.springframework.yarn.fs.ResourceLocalizer;

public final class YarnResourceLocalizerBuilder
		extends AbstractConfiguredAnnotationBuilder<ResourceLocalizer, AnnotationBuilder<ResourceLocalizer>> {

	private YarnConfiguration configuration;

	public YarnResourceLocalizerBuilder(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	public YarnResourceLocalizerBuilder(boolean allowConfigurersOfSameType, YarnConfiguration configuration) {
		super(allowConfigurersOfSameType);
		this.configuration = configuration;
	}

	@Override
	protected ResourceLocalizer performBuild() throws Exception {
		LocalResourcesFactoryBean fb = new LocalResourcesFactoryBean();
		fb.setConfiguration(configuration);
		fb.setHdfsEntries(new ArrayList<LocalResourcesFactoryBean.TransferEntry>());
		fb.afterPropertiesSet();
		return fb.getObject();
	}

	public void configuration(YarnConfiguration configuration) {
		this.configuration = configuration;
	}


}
