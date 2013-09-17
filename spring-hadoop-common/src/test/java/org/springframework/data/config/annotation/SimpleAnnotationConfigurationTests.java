/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.config.annotation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.config.annotation.simple.EnableSimpleTest;
import org.springframework.data.config.annotation.simple.SimpleTestConfig;
import org.springframework.data.config.annotation.simple.SimpleTestConfigBeanABuilder;
import org.springframework.data.config.annotation.simple.SimpleTestConfigBuilder;
import org.springframework.data.config.annotation.simple.SimpleTestConfigurerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class SimpleAnnotationConfigurationTests {

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void testSimpleConfig() throws Exception {
		assertNotNull(ctx);
		assertTrue(ctx.containsBean("simpleConfig"));
		SimpleTestConfig config = ctx.getBean("simpleConfig", SimpleTestConfig.class);
		assertTrue(config.simpleData.equals("foo"));
		assertNotNull(config.simpleProperties.getProperty("foo"));
		assertTrue(config.simpleProperties.getProperty("foo").equals("jee"));
	}

	@Configuration
	@EnableSimpleTest
	static class Config extends SimpleTestConfigurerAdapter {

		@Override
		public void configure(SimpleTestConfigBuilder config) throws Exception {
			config.withProperties().property("foo", "jee");
		}

		@Override
		public void configure(SimpleTestConfigBeanABuilder a) {
		}

//		@Override
//		public void init(SimpleTestConfigBuilder config) throws Exception {
//			config.setSharedObject(sharedType, object);
//		}

	}

}
