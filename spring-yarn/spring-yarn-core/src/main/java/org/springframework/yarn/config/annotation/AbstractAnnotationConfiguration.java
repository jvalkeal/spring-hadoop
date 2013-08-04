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
package org.springframework.yarn.config.annotation;

import java.util.List;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

public abstract class AbstractAnnotationConfiguration<B extends AnnotationBuilder<O>, O>
		implements ImportAware, BeanClassLoaderAware {

	private List<AnnotationConfigurer<O,B>> configurers;

	private ClassLoader beanClassLoader;

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		beanClassLoader = classLoader;
	}

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
	}

	@Autowired(required=false)
	public void setConfigurers(List<AnnotationConfigurer<O, B>> configurers) throws Exception {
		this.configurers = configurers;
		onConfigurers(configurers);
	}

	protected abstract void onConfigurers(List<AnnotationConfigurer<O, B>> configurers) throws Exception;

}
