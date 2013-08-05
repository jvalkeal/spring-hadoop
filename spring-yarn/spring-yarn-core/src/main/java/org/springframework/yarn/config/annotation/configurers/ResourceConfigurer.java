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

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;

public class ResourceConfigurer <O, B extends AnnotationBuilder<O>> extends AnnotationConfigurerAdapter<O,B> {

	private Set<Resource> resources = new HashSet<Resource>();
	private final DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

	@Override
	public void configure(B builder) throws Exception {
		if (!configureResources(builder, resources)) {
			if (builder instanceof ResourceConfigureAware) {
				((ResourceConfigureAware)builder).configureResources(resources);
			}
		}
	}

	public ResourceConfigurer<O,B> add(Set<Resource> resources) {
		this.resources.addAll(resources);
		return this;
	}

	public ResourceConfigurer<O,B> add(Resource resource) {
		resources.add(resource);
		return this;
	}

	public ResourceConfigurer<O,B> add(String resource) {
		resources.add(resourceLoader.getResource(resource));
		return this;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	protected boolean configureResources(B builder, Set<Resource> resources){
		return false;
	};

}
