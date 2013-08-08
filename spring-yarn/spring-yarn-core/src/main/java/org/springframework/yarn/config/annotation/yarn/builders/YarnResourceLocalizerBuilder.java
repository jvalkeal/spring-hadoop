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
package org.springframework.yarn.config.annotation.yarn.builders;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.yarn.config.annotation.AbstractConfiguredAnnotationBuilder;
import org.springframework.yarn.config.annotation.yarn.configurers.LocalResourcesCopyConfigurer;
import org.springframework.yarn.config.annotation.yarn.configurers.LocalResourcesHdfsConfigurer;
import org.springframework.yarn.fs.LocalResourcesFactoryBean;
import org.springframework.yarn.fs.LocalResourcesFactoryBean.CopyEntry;
import org.springframework.yarn.fs.LocalResourcesFactoryBean.TransferEntry;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * {@link AnnotationBuilder} for {@link ResourceLocalizer}.
 *
 * @author Janne Valkealahti
 *
 */
public final class YarnResourceLocalizerBuilder extends AbstractConfiguredAnnotationBuilder<ResourceLocalizer, YarnResourceLocalizerBuilder> {

	private Configuration configuration;
	private LocalResourceType defaultType = LocalResourceType.FILE;
	private LocalResourceVisibility defaultVisibility = LocalResourceVisibility.APPLICATION;
	private Collection<CopyEntry> copyEntries;
	private Collection<TransferEntry> transferEntries;

	public YarnResourceLocalizerBuilder(boolean allowConfigurersOfSameType) {
		super(allowConfigurersOfSameType);
	}

	public YarnResourceLocalizerBuilder(boolean allowConfigurersOfSameType, YarnConfiguration configuration) {
		super(allowConfigurersOfSameType);
		this.configuration = configuration;
	}

	@Override
	protected ResourceLocalizer performBuild() throws Exception {
		LocalResourcesFactoryBean fb = new LocalResourcesFactoryBean();
		fb.setType(defaultType);
		fb.setVisibility(defaultVisibility);
		fb.setConfiguration(configuration);
		fb.setCopyEntries(copyEntries);
		fb.setHdfsEntries(transferEntries);
		fb.setHdfsEntries(new ArrayList<LocalResourcesFactoryBean.TransferEntry>());
		fb.afterPropertiesSet();
		return fb.getObject();
	}

	public void configuration(Configuration configuration) {
		this.configuration = configuration;
	}

	public YarnResourceLocalizerBuilder defaultLocalResourceType(LocalResourceType type) {
		defaultType = type;
		return this;
	}

	public YarnResourceLocalizerBuilder defaultLocalResourceVisibility(LocalResourceVisibility visibility) {
		defaultVisibility = visibility;
		return this;
	}

	public void setCopyEntries(Collection<CopyEntry> copyEntries) {
		this.copyEntries = copyEntries;
	}

	public void setHdfsEntries(Collection<TransferEntry> transferEntries) {
		this.transferEntries = transferEntries;
	}

	public LocalResourcesCopyConfigurer withCopy() throws Exception {
		return apply(new LocalResourcesCopyConfigurer());
	}

	public LocalResourcesHdfsConfigurer withHdfs() throws Exception {
		return apply(new LocalResourcesHdfsConfigurer());
	}

}
