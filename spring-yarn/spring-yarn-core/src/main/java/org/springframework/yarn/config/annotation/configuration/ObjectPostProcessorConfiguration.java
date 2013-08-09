package org.springframework.yarn.config.annotation.configuration;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.yarn.config.annotation.ObjectPostProcessor;

/**
 * Spring {@link Configuration} that exports the default
 * {@link ObjectPostProcessor}. This class is not intended to be imported
 * manually rather it is imported automatically when using
 * {@link EnableWebSecurity} or {@link EnableGlobalMethodSecurity}.
 *
 * @see EnableWebSecurity
 * @see EnableGlobalMethodSecurity
 *
 * @author Rob Winch
 */
@Configuration
public class ObjectPostProcessorConfiguration {

    @Bean
    public ObjectPostProcessor<Object> objectPostProcessor(AutowireCapableBeanFactory beanFactory) {
        return new AutowireBeanFactoryObjectPostProcessor(beanFactory);
    }
}
