package com.github.yin.html.modules;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yin.html.jetty.WebServiceHandler;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.googlecode.jsonrpc4j.JsonRpcServer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.eclipse.jetty.server.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constructs a {@link JsonRpcServer} and {@link WebServiceHandler} for jetty.
 * See http://www.eclipse.org/jetty/documentation/9.3.x/embedded-examples.html
 */
public class JsonRpcServerModule extends AbstractModule {
	private final Module serviceModule;
	private static final Logger log  = LoggerFactory.getLogger(JsonRpcServerModule.class);

	/** Marks the backend service instance receiving RPC calls. */
	@BindingAnnotation
	@Retention(RUNTIME) @Target({FIELD, PARAMETER, METHOD})
	public @interface RpcService {
	}

	/** Marks the service interface implemented by the service. */
	@BindingAnnotation
	@Retention(RUNTIME) @Target({FIELD, PARAMETER, METHOD})
	public @interface RpcInterface {
	}

	public JsonRpcServerModule(Module serviceModule) {
		log.info("jsonrpc module created");
		this.serviceModule = serviceModule;
	}

	@Override
	protected void configure() {
		log.info("jsonrpc module config, with service-module: {}", serviceModule);
		install(serviceModule);
		install(new JacksonModule());
		Multibinder<Handler> multi = Multibinder.newSetBinder(binder(), Handler.class);
		multi.addBinding().to(WebServiceHandler.class);
	}

	@Provides
	public JsonRpcServer createJsonRpc(ObjectMapper mapper, @RpcService Object service, @RpcInterface Class iface) {
		log.info("creating jsonrpc server from service: mapper:{} iface:{} service:{}", mapper, iface, service);
		JsonRpcServer handler = new JsonRpcServer(mapper, service, iface);
		return handler;
	}

	@Provides
	public WebServiceHandler createRpcHandler(JsonRpcServer jsonrpc) {
		log.info("creating jetty handler for jsonrpc server: {}", jsonrpc);
		return new WebServiceHandler(jsonrpc);
	}
}