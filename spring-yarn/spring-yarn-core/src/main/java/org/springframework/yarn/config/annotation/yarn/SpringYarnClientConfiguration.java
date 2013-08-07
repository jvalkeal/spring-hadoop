package org.springframework.yarn.config.annotation.yarn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.client.YarnClient;

@Configuration
public class SpringYarnClientConfiguration extends SpringYarnConfiguration {

	@Bean(name=YarnSystemConstants.DEFAULT_ID_CLIENT)
	@DependsOn(YarnSystemConstants.DEFAULT_ID_CONFIGURATION)
	public YarnClient yarnClient() throws Exception {
		return builder.getOrBuild().getYarnClient();
	}


}
