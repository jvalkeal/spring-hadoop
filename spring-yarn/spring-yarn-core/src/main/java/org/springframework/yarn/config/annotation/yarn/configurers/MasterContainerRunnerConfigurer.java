package org.springframework.yarn.config.annotation.yarn.configurers;

import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.CommandLineAppmasterRunner;
import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.config.annotation.yarn.builders.YarnAppmasterBuilder;
import org.springframework.yarn.container.CommandLineContainerRunner;
import org.springframework.yarn.launch.LaunchCommandsFactoryBean;

public class MasterContainerRunnerConfigurer extends AnnotationConfigurerAdapter<YarnAppmaster, YarnAppmasterBuilder> {

	private Class<?> clazz;

	@Override
	public void configure(YarnAppmasterBuilder builder) throws Exception {
		LaunchCommandsFactoryBean fb = new LaunchCommandsFactoryBean();
		fb.setRunner(CommandLineContainerRunner.class);
		fb.setContextFile(clazz.getCanonicalName());
		fb.setBeanName(YarnSystemConstants.DEFAULT_ID_CONTAINER);
		fb.setStdout("<LOG_DIR>/Container.stdout");
		fb.setStderr("<LOG_DIR>/Container.stderr");
		fb.afterPropertiesSet();
		builder.setCommands(fb.getObject());
	}

	public MasterContainerRunnerConfigurer clazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

}
