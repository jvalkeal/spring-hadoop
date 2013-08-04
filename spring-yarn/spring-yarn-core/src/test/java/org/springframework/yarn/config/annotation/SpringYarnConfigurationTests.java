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
package org.springframework.yarn.config.annotation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.config.annotation.yarn.EnableYarn;
import org.springframework.yarn.config.annotation.yarn.SpringYarnConfigurerAdapter;
import org.springframework.yarn.config.annotation.yarn.builders.SpringYarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnConfigBuilder;
import org.springframework.yarn.config.annotation.yarn.builders.YarnResourceLocalizerBuilder;
import org.springframework.yarn.fs.ResourceLocalizer;
//import org.springframework.yarn.config.annotation.yarn.EnableYarn;
//import org.springframework.yarn.config.annotation.yarn.YarnConfigConfigurerAdapter;
import org.springframework.yarn.test.context.YarnDelegatingSmartContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=YarnDelegatingSmartContextLoader.class)
public class SpringYarnConfigurationTests {

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void testSimpleConfig() {
		assertNotNull(ctx);
		assertTrue(ctx.containsBean("yarnConfiguration"));
		Configuration config = (Configuration) ctx.getBean("yarnConfiguration");
		assertNotNull(config);
		assertTrue(ctx.containsBean("yarnLocalresources"));
		ResourceLocalizer localizer = (ResourceLocalizer) ctx.getBean("yarnLocalresources");
		assertNotNull(localizer);

		assertThat(config.get("resource.property"), is("test-site-1.xml"));
		assertThat(config.get("resource.property.2"), is("test-site-2.xml"));
		assertThat(config.get("foo"), is("jee"));
	}

	@org.springframework.context.annotation.Configuration
	@EnableYarn
	static class Config extends SpringYarnConfigurerAdapter {

		@Override
		protected void configure(YarnConfigBuilder config) throws Exception {
			config
				.withResource("classpath:/test-site-1.xml")
				.withProperties().add("foo", "jee").and()
				.withResource("classpath:/test-site-2.xml");

			int foo = 1;
			foo = 2;
				//.withProperties()
				//	.key().value()
		}

		@Override
		protected void configure(YarnResourceLocalizerBuilder localizer) {
//			localizer.
		}

	}

}
