package org.springframework.yarn.config.annotation.yarn.configurers;

import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.config.annotation.yarn.builders.YarnAppmasterBuilder;

public class MasterContainerRunnerConfigurer extends AnnotationConfigurerAdapter<YarnAppmaster, YarnAppmasterBuilder> {

	@Override
	public void configure(YarnAppmasterBuilder builder) throws Exception {

	}

}
