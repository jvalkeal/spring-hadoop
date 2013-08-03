package org.springframework.yarn.config.annotation.yarn.configurers;

import java.util.Properties;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.configurers.AbstractPropertiesConfigurer;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfigBuilder;

public class YarnConfigPropertiesConfigurer
		extends AbstractPropertiesConfigurer<YarnConfigBuilder, YarnConfiguration, AnnotationBuilder<YarnConfiguration>> {

	@Override
	protected void configureProperties(YarnConfigBuilder builder, Properties properties) {
		builder.getProperties().putAll(properties);
	}

}
