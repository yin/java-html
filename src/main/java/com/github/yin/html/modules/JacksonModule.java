package com.github.yin.html.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.StringJoiner;

/**
 * Configures default jackson classes with Guava immutable collection support.
 */
public class JacksonModule extends AbstractModule {

	private Logger log = LoggerFactory.getLogger(JacksonModule.class);

	public void configure() {
		Multibinder multi = Multibinder.newSetBinder(binder(), com.fasterxml.jackson.databind.Module.class);
		multi.addBinding().to(GuavaModule.class);
	}

	@Provides
	public ObjectMapper createObjectMapper(Set<com.fasterxml.jackson.databind.Module> jacksonModules) {
		log.debug("jackson-modules: {}", jacksonModules);
		return new ObjectMapper().registerModules(jacksonModules);
	}
}