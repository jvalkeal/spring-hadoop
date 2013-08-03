package org.springframework.yarn.config.annotation.configurers;

import java.util.Properties;

import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;

public abstract class AbstractPropertiesConfigurer <T extends AnnotationBuilder<O>, O, B extends AnnotationBuilder<O>>
		extends AnnotationConfigurerAdapter<O,B> {

	private Properties properties = new Properties();

	public AbstractPropertiesConfigurer<T,O,B> add(Properties properties) {
		properties.putAll(properties);
		return this;
	}

	public AbstractPropertiesConfigurer<T,O,B> add(String key, String value) {
		properties.put(key, value);
		return this;
	}

	public Properties getProperties() {
		return properties;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configure(B builder) throws Exception {
		configureProperties((T)builder, properties);
	}

	protected abstract void configureProperties(T builder, Properties properties);

}
