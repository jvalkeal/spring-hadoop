package org.springframework.yarn.config.annotation.yarn.configurers;

import java.util.Properties;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.configurers.PropertiesConfigurer;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfigBuilder;

//extends PropertiesConfigurer<YarnConfigBuilder, YarnConfiguration, AnnotationBuilder<YarnConfiguration>>
//

public class YarnConfigPropertiesConfigurer
		extends PropertiesConfigurer<YarnConfiguration, YarnConfigBuilder> {

	@Override
	protected void configureProperties(YarnConfigBuilder builder, Properties properties) {
		builder.getProperties().putAll(properties);
	}

//	@Override
//	public AnnotationBuilder<YarnConfiguration> and() {
//		return super.and();
//	}


}
