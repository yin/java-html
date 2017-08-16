package com.github.yin.html.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;

import java.util.Set;

/**
 * Configures default jackson classes with Guava immutable collection support.
 */
public class JacksonModule extends AbstractModule {

	public void configure() {
		Multibinder multi = Multibinder.newSetBinder(binder(), com.fasterxml.jackson.databind.Module.class);
		multi.addBinding().to(GuavaModule.class);
	}

	@Provides
	public ObjectMapper createObjectMapper(Set<com.fasterxml.jackson.databind.Module> jacksonModules) {
		return new ObjectMapper().registerModules(jacksonModules);
	}
}