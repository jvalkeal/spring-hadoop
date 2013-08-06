package org.springframework.yarn.config.annotation.yarn.builders;

import java.util.Map;

import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.configuration.EnvironmentFactoryBean;

public final class YarnEnvironment extends AbstractConfiguredAnnotationBuilder<Map<String, String>, YarnEnvironment> {

	public YarnEnvironment() {
		this(true);
	}

	public YarnEnvironment(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	@Override
	protected Map<String, String> performBuild() throws Exception {
		EnvironmentFactoryBean fb = new EnvironmentFactoryBean();
		fb.afterPropertiesSet();
		return fb.getObject();
	}

}
