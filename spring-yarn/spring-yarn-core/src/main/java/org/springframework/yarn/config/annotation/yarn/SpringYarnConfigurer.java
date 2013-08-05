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

import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationConfigurer;

/**
 * Allows for configuring a {@link AnnotationBuilder}. All
 * {@link AnnotationConfigurer} first have their {@link #init(AnnotationBuilder)}
 * method invoked. After all {@link #init(AnnotationBuilder)} methods have been
 * invoked, each {@link #configure(AnnotationBuilder)} method is invoked.
 *
 * @author Janne Valkealahti
 * @see AbstractConfiguredAnnotationBuilder
 *
 * @param <O> The object being built by the {@link AnnotationBuilder} B
 * @param <B> The {@link AnnotationBuilder} that builds objects of type O. This is
 *            also the {@link AnnotationBuilder} that is being configured.
 */
public interface SpringYarnConfigurer<B extends AnnotationBuilder<SpringYarnConfigs>> extends
		AnnotationConfigurer<SpringYarnConfigs, B> {

}
