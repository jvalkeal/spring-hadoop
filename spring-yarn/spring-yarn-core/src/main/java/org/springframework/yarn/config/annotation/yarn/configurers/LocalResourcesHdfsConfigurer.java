package org.springframework.yarn.config.annotation.yarn.configurers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.yarn.config.annotation.AnnotationConfigurerAdapter;
import org.springframework.yarn.config.annotation.yarn.builders.YarnResourceLocalizerBuilder;
import org.springframework.yarn.fs.LocalResourcesFactoryBean.TransferEntry;
import org.springframework.yarn.fs.ResourceLocalizer;

public class LocalResourcesHdfsConfigurer extends AnnotationConfigurerAdapter<ResourceLocalizer, YarnResourceLocalizerBuilder> {

	private Collection<TransferEntry> hdfsEntries = new ArrayList<TransferEntry>();

	@Override
	public void configure(YarnResourceLocalizerBuilder builder) throws Exception {
		builder.setHdfsEntries(hdfsEntries);
	}

	public LocalResourcesHdfsConfigurer hdfs(String path) {
		hdfsEntries.add(new TransferEntry(null, null, path, null, null, false));
		return this;
	}

}
