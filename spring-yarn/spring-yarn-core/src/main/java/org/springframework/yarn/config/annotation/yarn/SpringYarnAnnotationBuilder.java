package org.springframework.yarn.config.annotation.yarn;

import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationConfigurer;

public interface SpringYarnAnnotationBuilder<H extends SpringYarnAnnotationBuilder<H>>
		extends AnnotationBuilder<SpringYarnConfig> {

	<C extends AnnotationConfigurer<SpringYarnConfig, H>> C getConfigurer(Class<C> clazz);


}
