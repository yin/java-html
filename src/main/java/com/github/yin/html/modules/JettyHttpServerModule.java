package com.github.yin.html.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import org.eclipse.jetty.server.Server;

public class JettyHttpServerModule extends AbstractModule {
	private final int port;

	public JettyHttpServerModule() {
		this(8080);
	}

	public JettyHttpServerModule(int port) {
		this.port = port;
	}
	@Override
	protected void configure() {
	}

	@Provides
	public Server createServer() {
		Server jetty = new Server(port);
		return jetty;
	}
}
