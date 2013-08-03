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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * <p>A base {@link AnnotationBuilder} that allows {@link AnnotationConfigurer} to be
 * applied to it. This makes modifying the {@link AnnotationBuilder} a strategy
 * that can be customized and broken up into a number of
 * {@link AnnotationConfigurer} objects that have more specific goals than that
 * of the {@link AnnotationBuilder}.</p>
 *
 * @author Rob Winch
 * @author Janne Valkealahti
 *
 * @param <O> The object that this builder returns
 * @param <B> The type of this builder (that is returned by the base class)
 */
public abstract class AbstractConfiguredAnnotationBuilder<O, B extends AnnotationBuilder<O>>
		extends AbstractAnnotationBuilder<O> {

	private final static Log log = LogFactory.getLog(AbstractConfiguredAnnotationBuilder.class);

	private final LinkedHashMap<Class<? extends AnnotationConfigurer<O, B>>, List<AnnotationConfigurer<O, B>>> configurers = new LinkedHashMap<Class<? extends AnnotationConfigurer<O, B>>, List<AnnotationConfigurer<O, B>>>();

	private final Map<Class<Object>, Object> sharedObjects = new HashMap<Class<Object>, Object>();

	private final boolean allowConfigurersOfSameType;

	private BuildState buildState = BuildState.UNBUILT;

	protected AbstractConfiguredAnnotationBuilder(boolean allowConfigurersOfSameType) {
		this.allowConfigurersOfSameType = allowConfigurersOfSameType;
	}

	public O getOrBuild() {
		if (isUnbuilt()) {
			try {
				return build();
			} catch (Exception e) {
				log.debug("Failed to perform build. Returning null", e);
				return null;
			}
		} else {
			return getObject();
		}
	}

	@SuppressWarnings("unchecked")
	public <C extends AnnotationConfigurerAdapter<O, B>> C apply(C configurer) throws Exception {
		add(configurer);
		configurer.setBuilder((B) this);
		return configurer;
	}

	public <C extends AnnotationConfigurer<O, B>> C apply(C configurer) throws Exception {
		add(configurer);
		return configurer;
	}

	@SuppressWarnings("unchecked")
	public <C> void setSharedObject(Class<C> sharedType, C object) {
		this.sharedObjects.put((Class<Object>) sharedType, object);
	}

	@SuppressWarnings("unchecked")
	public <C> C getSharedObject(Class<C> sharedType) {
		return (C) this.sharedObjects.get(sharedType);
	}

	public Map<Class<Object>, Object> getSharedObjects() {
		return Collections.unmodifiableMap(this.sharedObjects);
	}

	@SuppressWarnings("unchecked")
	private <C extends AnnotationConfigurer<O, B>> void add(C configurer) throws Exception {
		Assert.notNull(configurer, "configurer cannot be null");

		Class<? extends AnnotationConfigurer<O, B>> clazz = (Class<? extends AnnotationConfigurer<O, B>>) configurer
				.getClass();
		synchronized (configurers) {
			if (buildState.isConfigured()) {
				throw new IllegalStateException("Cannot apply " + configurer + " to already built object");
			}
			List<AnnotationConfigurer<O, B>> configs = allowConfigurersOfSameType ? this.configurers.get(clazz) : null;
			if (configs == null) {
				configs = new ArrayList<AnnotationConfigurer<O, B>>(1);
			}
			configs.add(configurer);
			this.configurers.put(clazz, configs);
			if (buildState.isInitializing()) {
				configurer.init((B) this);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <C extends AnnotationConfigurer<O, B>> List<C> getConfigurers(Class<C> clazz) {
		List<C> configs = (List<C>) this.configurers.get(clazz);
		if (configs == null) {
			return new ArrayList<C>();
		}
		return new ArrayList<C>(configs);
	}

	@SuppressWarnings("unchecked")
	public <C extends AnnotationConfigurer<O, B>> List<C> removeConfigurers(Class<C> clazz) {
		List<C> configs = (List<C>) this.configurers.remove(clazz);
		if (configs == null) {
			return new ArrayList<C>();
		}
		return new ArrayList<C>(configs);
	}

	@SuppressWarnings("unchecked")
	public <C extends AnnotationConfigurer<O, B>> C getConfigurer(Class<C> clazz) {
		List<AnnotationConfigurer<O, B>> configs = this.configurers.get(clazz);
		if (configs == null) {
			return null;
		}
		if (configs.size() != 1) {
			throw new IllegalStateException("Only one configurer expected for type " + clazz + ", but got " + configs);
		}
		return (C) configs.get(0);
	}

	@SuppressWarnings("unchecked")
	public <C extends AnnotationConfigurer<O, B>> C removeConfigurer(Class<C> clazz) {
		List<AnnotationConfigurer<O, B>> configs = this.configurers.remove(clazz);
		if (configs == null) {
			return null;
		}
		if (configs.size() != 1) {
			throw new IllegalStateException("Only one configurer expected for type " + clazz + ", but got " + configs);
		}
		return (C) configs.get(0);
	}

	@Override
	protected final O doBuild() throws Exception {
		synchronized (configurers) {
			buildState = BuildState.INITIALIZING;

			beforeInit();
			init();

			buildState = BuildState.CONFIGURING;

			beforeConfigure();
			configure();

			buildState = BuildState.BUILDING;

			O result = performBuild();

			buildState = BuildState.BUILT;

			return result;
		}
	}

	protected void beforeInit() throws Exception {
	}

	protected void beforeConfigure() throws Exception {
	}

	protected abstract O performBuild() throws Exception;

	@SuppressWarnings("unchecked")
	private void init() throws Exception {
		Collection<AnnotationConfigurer<O, B>> configurers = getConfigurers();

		for (AnnotationConfigurer<O, B> configurer : configurers) {
			configurer.init((B) this);
		}
	}

	@SuppressWarnings("unchecked")
	private void configure() throws Exception {
		Collection<AnnotationConfigurer<O, B>> configurers = getConfigurers();

		for (AnnotationConfigurer<O, B> configurer : configurers) {
			configurer.configure((B) this);
		}
	}

	private Collection<AnnotationConfigurer<O, B>> getConfigurers() {
		List<AnnotationConfigurer<O, B>> result = new ArrayList<AnnotationConfigurer<O, B>>();
		for (List<AnnotationConfigurer<O, B>> configs : this.configurers.values()) {
			result.addAll(configs);
		}
		return result;
	}

	private boolean isUnbuilt() {
		synchronized (configurers) {
			return buildState == BuildState.UNBUILT;
		}
	}

	/**
	 * The build state for the application
	 */
	private static enum BuildState {
		/**
		 * This is the state before the {@link Builder#build()} is invoked
		 */
		UNBUILT(0),

		/**
		 * The state from when {@link Builder#build()} is first invoked until
		 * all the {@link SecurityConfigurer#init(SecurityBuilder)} methods have
		 * been invoked.
		 */
		INITIALIZING(1),
		/**
		 * The state from after all
		 * {@link SecurityConfigurer#init(SecurityBuilder)} have been invoked
		 * until after all the
		 * {@link SecurityConfigurer#configure(SecurityBuilder)} methods have
		 * been invoked.
		 */
		CONFIGURING(2),

		/**
		 * From the point after all the
		 * {@link SecurityConfigurer#configure(SecurityBuilder)} have completed
		 * to just after
		 * {@link AbstractConfiguredSecurityBuilder#performBuild()}.
		 */
		BUILDING(3),

		/**
		 * After the object has been completely built.
		 */
		BUILT(4);

		private final int order;

		BuildState(int order) {
			this.order = order;
		}

		public boolean isInitializing() {
			return INITIALIZING.order == order;
		}

		/**
		 * Determines if the state is CONFIGURING or later
		 *
		 * @return
		 */
		public boolean isConfigured() {
			return order >= CONFIGURING.order;
		}
	}

}
