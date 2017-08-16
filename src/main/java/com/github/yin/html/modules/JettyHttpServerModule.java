package com.github.yin.html.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.Set;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JettyHttpServerModule extends AbstractModule {
	/*private final int port;
	private static final Logger log = LoggerFactory.getLogger(JettyHttpServerModule.class);

	public JettyHttpServerModule() {
		this(8080);
	}

	public JettyHttpServerModule(int port) {
		this.port = port;
	}
*/
	@Override
	protected void configure() {

	}
/*
	@Provides
	public Server createServer(Set<Handler> handlers) {
		log.info("creating jetty with handlers: {}", handlers);
		Server jetty = new Server(port);
		// Add the ResourceHandler to the jetty.
		HandlerList handlerList = new HandlerList();
		handlerList.setHandlers(constructArray(Handler.class, handlers, new DefaultHandler()));
		jetty.setHandler(handlerList);
		return jetty;
	}
	*/
}
