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

import org.springframework.data.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.container.YarnContainer;
import org.springframework.yarn.container.YarnContainerFactoryBean;

public final class YarnContainerBuilder extends AbstractConfiguredAnnotationBuilder<YarnContainer, YarnContainerBuilder> {

	private Class<? extends YarnContainer> clazz;

	public YarnContainerBuilder() {
	}

	@Override
	protected YarnContainer performBuild() throws Exception {
		YarnContainerFactoryBean fb = new YarnContainerFactoryBean();
		fb.setContainerClass(clazz);
		fb.afterPropertiesSet();
		return fb.getObject();
	}

	public YarnContainerBuilder clazz(Class<? extends YarnContainer> clazz) {
		this.clazz = clazz;
		return this;
	}

}
