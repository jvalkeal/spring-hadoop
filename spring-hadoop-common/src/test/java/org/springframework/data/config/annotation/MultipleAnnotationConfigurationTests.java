package org.springframework.data.config.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.config.annotation.complex.ComplexTestConfigBeanABuilder;
import org.springframework.data.config.annotation.complex.ComplexTestConfigBeanBConfigure;
import org.springframework.data.config.annotation.complex.ComplexTestConfigBuilder;
import org.springframework.data.config.annotation.complex.ComplexTestConfigurerAdapter;
import org.springframework.data.config.annotation.complex.EnableComplexTest;
import org.springframework.data.config.annotation.simple.EnableSimpleTest;
import org.springframework.data.config.annotation.simple.SimpleTestConfigBeanABuilder;
import org.springframework.data.config.annotation.simple.SimpleTestConfigBeanBConfigure;
import org.springframework.data.config.annotation.simple.SimpleTestConfigBuilder;
import org.springframework.data.config.annotation.simple.SimpleTestConfigurerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class MultipleAnnotationConfigurationTests {

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void testConfig() throws Exception {

	}

	@Configuration
	@EnableSimpleTest
	static class SimpleConfig extends SimpleTestConfigurerAdapter {
	}


	@Configuration
	@EnableComplexTest
	static class ComplexConfig extends ComplexTestConfigurerAdapter {
	}

}
