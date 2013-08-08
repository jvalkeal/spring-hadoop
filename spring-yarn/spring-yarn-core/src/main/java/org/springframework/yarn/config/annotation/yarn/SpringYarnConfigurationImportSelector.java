package org.springframework.yarn.config.annotation.yarn;

import java.util.Map;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.yarn.config.annotation.yarn.EnableYarn.Enable;
import org.springframework.yarn.config.annotation.yarn.configuration.SpringYarnClientConfiguration;
import org.springframework.yarn.config.annotation.yarn.configuration.SpringYarnConfiguration;

public class SpringYarnConfigurationImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {

		Map<String, Object> enableConfigurationAttrMap =
				importingClassMetadata.getAnnotationAttributes(EnableYarn.class.getName());
		AnnotationAttributes enableConfigurationAttrs = AnnotationAttributes.fromMap(enableConfigurationAttrMap);

		Enable enable = enableConfigurationAttrs.getEnum("enable");

		if (enable == Enable.CLIENT) {
			return new String[]{
					SpringYarnClientConfiguration.class.getName(),
					ConfiguringBeanFactoryPostProcessorConfiguration.class.getName()};
		} else {
			return new String[]{
					SpringYarnConfiguration.class.getName(),
					ConfiguringBeanFactoryPostProcessorConfiguration.class.getName()};
		}

	}


}
