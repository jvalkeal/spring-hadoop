/*
 * Copyright 2014 the original author or authors.
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
package org.springframework.data.hadoop.config.common.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * Base class for {@link Configuration} which works on a bean definition level
 * relying on {@link ImportBeanDefinitionRegistrar} phase to register beans.
 *
 * @author Janne Valkealahti
 *
 * @param <B>
 * @param <O>
 */
public abstract class AbstractImportingAnnotationConfiguration<B extends AnnotationBuilder<O>, O> implements
		ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {

	private BeanFactory beanFactory;

	private Environment environment;

	@SuppressWarnings("unchecked")
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		ListableBeanFactory f = (ListableBeanFactory) getBeanFactory();
		List<AnnotationConfigurer<O, B>> configurers = new ArrayList<AnnotationConfigurer<O,B>>();

		Class<?> annotationType = getAnnotation();
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(
				annotationType.getName(), false));
		String[] names = attributes.getStringArray("name");

		Map<String, Object> beansWithAnnotation = f.getBeansWithAnnotation(getAnnotation());
		for (Entry<String, Object> e : beansWithAnnotation.entrySet()) {
			if (e.getValue().getClass().getName().equals(importingClassMetadata.getClassName())) {
				configurers.add((AnnotationConfigurer<O, B>) e.getValue());
			}
		}

		try {
			onConfigurers(registry, names, configurers);
		} catch (Exception e) {
			throw new RuntimeException("error", e);
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Assert.isInstanceOf(ListableBeanFactory.class, beanFactory,
				"beanFactory be of type ListableBeanFactory but was " + beanFactory);
		this.beanFactory = beanFactory;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	protected abstract void onConfigurers(BeanDefinitionRegistry registry, String[] names, List<AnnotationConfigurer<O, B>> configurers) throws Exception;

	protected abstract Class<? extends Annotation> getAnnotation();

	/**
	 * Gets the bean factory.
	 *
	 * @return the bean factory
	 */
	protected BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * Gets the environment.
	 *
	 * @return the environment
	 */
	protected Environment getEnvironment() {
		return environment;
	}

}
