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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.yarn.config.annotation.ObjectPostProcessor;
import org.springframework.yarn.config.annotation.yarn.EnableYarn.Enable;
import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnAppmasterBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnClientBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnContainerBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnEnvironmentBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnResourceLocalizerBuilder;

/**
 * Provides a convenient base class for creating a {@link SpringYarnConfigurer}
 * instance. The implementation allows customization by overriding methods.
 *
 * @author Janne Valkealahti
 * @see EnableYarn
 */
public class SpringYarnConfigurerAdapter implements SpringYarnConfigurer {

	private final static Log log = LogFactory.getLog(SpringYarnConfigurerAdapter.class);

	private YarnConfigBuilder yarnConfigBuilder;
	private YarnResourceLocalizerBuilder yarnResourceLocalizerBuilder;
	private YarnEnvironmentBuilder yarnEnvironmentBuilder;
	private YarnClientBuilder yarnClientBuilder;
	private YarnAppmasterBuilder yarnAppmasterBuilder;
	private YarnContainerBuilder yarnContainerBuilder;

    private ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
        @Override
        public <T> T postProcess(T object) {
            throw new IllegalStateException(ObjectPostProcessor.class.getName()
            		+ " is a required bean. Ensure you have used @EnableYarn and @Configuration");
        }
    };

    @Autowired(required=false)
    public void setObjectPostProcessor(ObjectPostProcessor<Object> objectPostProcessor) {
        this.objectPostProcessor = objectPostProcessor;
    }

	@Override
	public void init(SpringYarnConfigBuilder builder) throws Exception {
		builder.setYarnConfigBuilder(getConfigBuilder());
		builder.setYarnLocalizerBuilder(getLocalizerBuilder());
		builder.setEnvironmentBuilder(getEnvironmentBuilder());

		EnableYarn annotation = AnnotationUtils.findAnnotation(getClass(), EnableYarn.class);
		Enable enable = annotation.enable();

		if (log.isDebugEnabled()) {
			log.debug("Enabling builder for " + enable);
		}

		if (enable == Enable.CLIENT) {
			builder.setYarnClientBuilder(getClientBuilder());
		} else if (enable == Enable.APPMASTER) {
			builder.setYarnAppmasterBuilder(getAppmasterBuilder());
		} else if (enable == Enable.CONTAINER) {
			builder.setYarnContainerBuilder(getContainerBuilder());
		}
	}

	@Override
	public void configure(SpringYarnConfigBuilder builder) throws Exception {
	}

	@Override
    public void configure(YarnConfigBuilder config) throws Exception {
	}

	@Override
	public void configure(YarnResourceLocalizerBuilder localizer) throws Exception {
	}

	@Override
	public void configure(YarnEnvironmentBuilder environment) throws Exception {
	}

	@Override
	public void configure(YarnClientBuilder client) throws Exception {
	}

	@Override
	public void configure(YarnAppmasterBuilder master) throws Exception {
	}

	@Override
	public void configure(YarnContainerBuilder container) throws Exception {
	}

	/**
	 * Gets the Yarn config builder.
	 *
	 * @return the Yarn config builder
	 * @throws Exception if error occurred
	 */
	protected final YarnConfigBuilder getConfigBuilder() throws Exception {
		if (yarnConfigBuilder != null) {
			return yarnConfigBuilder;
		}
		yarnConfigBuilder = new YarnConfigBuilder(objectPostProcessor);
		configure(yarnConfigBuilder);
		return yarnConfigBuilder;
	}

	protected final YarnResourceLocalizerBuilder getLocalizerBuilder() throws Exception {
		if (yarnResourceLocalizerBuilder != null) {
			return yarnResourceLocalizerBuilder;
		}
		yarnResourceLocalizerBuilder = new YarnResourceLocalizerBuilder();
		configure(yarnResourceLocalizerBuilder);
		return yarnResourceLocalizerBuilder;
	}

	protected final YarnEnvironmentBuilder getEnvironmentBuilder() throws Exception {
		if (yarnEnvironmentBuilder != null) {
			return yarnEnvironmentBuilder;
		}
		yarnEnvironmentBuilder = new YarnEnvironmentBuilder();
		configure(yarnEnvironmentBuilder);
		return yarnEnvironmentBuilder;
	}

	protected final YarnClientBuilder getClientBuilder() throws Exception {
		if (yarnClientBuilder != null) {
			return yarnClientBuilder;
		}
		yarnClientBuilder = new YarnClientBuilder();
		configure(yarnClientBuilder);
		return yarnClientBuilder;
	}

	protected final YarnAppmasterBuilder getAppmasterBuilder() throws Exception {
		if (yarnAppmasterBuilder != null) {
			return yarnAppmasterBuilder;
		}
		yarnAppmasterBuilder = new YarnAppmasterBuilder(objectPostProcessor);
		configure(yarnAppmasterBuilder);
		return yarnAppmasterBuilder;
	}

	protected final YarnContainerBuilder getContainerBuilder() throws Exception {
		if (yarnContainerBuilder != null) {
			return yarnContainerBuilder;
		}
		yarnContainerBuilder = new YarnContainerBuilder();
		configure(yarnContainerBuilder);
		return yarnContainerBuilder;
	}

}
