package org.springframework.data.config.annotation.simple;

import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.data.config.annotation.AbstractConfiguredAnnotationBuilder;

public class SimpleTestConfigBeanABuilder extends AbstractConfiguredAnnotationBuilder<Set<Resource>, SimpleTestConfigBeanABuilder>{

	@Override
	protected Set<Resource> performBuild() throws Exception {
		return null;
	}

}
