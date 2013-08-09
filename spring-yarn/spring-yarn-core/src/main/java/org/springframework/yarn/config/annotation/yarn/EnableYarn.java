package org.springframework.yarn.config.annotation.yarn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.yarn.config.annotation.EnableAnnotationConfiguration;
import org.springframework.yarn.config.annotation.configuration.ObjectPostProcessorConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableAnnotationConfiguration
@Import({SpringYarnConfigurationImportSelector.class, ObjectPostProcessorConfiguration.class})
public @interface EnableYarn {

	Enable enable() default Enable.BASE;

	public enum Enable {
		BASE,CLIENT,APPMASTER,CONTAINER;
	}
}
