package org.springframework.yarn.config.annotation.yarn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.yarn.config.annotation.EnableAnnotationConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableAnnotationConfiguration
@Import({SpringYarnClientConfiguration.class, ConfiguringBeanFactoryPostProcessorConfiguration.class})
public @interface EnableYarnClient {

}
