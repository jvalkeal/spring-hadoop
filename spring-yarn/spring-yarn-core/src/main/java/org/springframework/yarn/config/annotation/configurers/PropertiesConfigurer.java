package org.springframework.yarn.config.annotation.configurers;

import java.util.Properties;

import org.springframework.yarn.config.annotation.AnnotationBuilder;
import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;

/**
 *
 * @author Janne Valkealahti
 *
 * @param <T>
 * @param <O>
 * @param <B>
 *
 * abstract class PropertiesConfigurer <T extends AnnotationBuilder<O>, O, B extends AnnotationBuilder<O>>
 */
public abstract class PropertiesConfigurer <O, B extends AnnotationBuilder<O>>
		extends AnnotationConfigurerAdapter<O,B> {

	private Properties properties = new Properties();

	public PropertiesConfigurer<O,B> add(Properties properties) {
		properties.putAll(properties);
		return this;
	}

	public PropertiesConfigurer<O,B> add(String key, String value) {
		properties.put(key, value);
		return this;
	}

	public Properties getProperties() {
		return properties;
	}

	@Override
	public void configure(B builder) throws Exception {
		configureProperties(builder, properties);
	}

	protected abstract void configureProperties(B builder, Properties properties);

}
