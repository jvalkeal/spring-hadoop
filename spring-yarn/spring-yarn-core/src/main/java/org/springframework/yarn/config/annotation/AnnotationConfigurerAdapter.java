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

/**
 * A base class for {@link AnnotationConfigurer} that allows subclasses to only
 * implement the methods they are interested in. It also provides a mechanism
 * for using the {@link AnnotationConfigurer} and when done gaining access to the
 * {@link AnnotationBuilder} that is being configured.
 *
 * @author Rob Winch
 * @author Janne Valkealahti
 *
 * @param <O> The Object being built by B
 * @param <B> The Builder that is building O and is configured by {@link AnnotationConfigurerAdapter}
 */
public abstract class AnnotationConfigurerAdapter<O, B extends AnnotationBuilder<O>> implements AnnotationConfigurer<O,B> {

	private B builder;

	@Override
	public void init(B builder) throws Exception {}

	@Override
	public void configure(B builder) throws Exception {}

	/**
	 * Return the {@link AnnotationBuilder} when done using the
	 * {@link AnnotationConfigurer}. This is useful for method chaining.
	 *
	 * @return the {@link AnnotationBuilder}
	 */
	public B and() {
		return getBuilder();
	}

	/**
	 * Gets the {@link AnnotationBuilder}. Cannot be null.
	 *
	 * @return the {@link AnnotationBuilder}
	 * @throw {@link IllegalStateException} if {@link AnnotationBuilder} is null
	 */
	protected final B getBuilder() {
		if(builder == null) {
			throw new IllegalStateException("annotationBuilder cannot be null");
		}
		return builder;
	}

	/**
	 * Sets the {@link AnnotationBuilder} to be used. This is automatically set
	 * when using
	 * {@link AbstractConfiguredAnnotationBuilder#apply(AnnotationConfigurerAdapter)}
	 *
	 * @param builder the {@link AnnotationBuilder} to set
	 */
	public void setBuilder(B builder) {
		this.builder = builder;
	}

}
