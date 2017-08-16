package com.github.yin.html.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yin.html.jetty.WebServiceHandler;
import com.github.yin.html.modules.JettyHttpServerModule;
import com.github.yin.html.modules.JsonRpcServerModule;
import com.github.yin.html.service.WebDocumentStatisticsService;
import com.github.yin.html.service.WebDocumentStatisticsServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.googlecode.jsonrpc4j.JsonRpcServer;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerMain {
	private static Logger log = LoggerFactory.getLogger(WebServerMain.class);

	public static void main(String[] args) throws Exception {
		//*
		Injector injector = Guice.createInjector(
				new JettyHttpServerModule(),
				new JsonRpcServerModule(new AbstractModule() {
					@Override
					protected void configure() {
						bind(Class.class).annotatedWith(JsonRpcServerModule.RpcInterface.class)
								.toInstance(WebDocumentStatisticsService.class);
						bind(Object.class).annotatedWith(JsonRpcServerModule.RpcService.class)
								.to(WebDocumentStatisticsServiceImpl.class);
					}
				}));
		//*/

		//*
		Server server = injector.getInstance(Server.class);
		/*/
		Server server = new Server(8080);
		//*/
		// Start things up! By using the server.join() the server thread will join with the current thread.
		// See "http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()" for more details.
		if (log.isInfoEnabled()) {
			log.info("Starting server");
		}

		/*
		ContextHandler webapp = new ContextHandler();
		webapp.setContextPath("/");
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setBaseResource(Resource.newClassPathResource("index.html"));
		webapp.setHandler(resourceHandler);
		*/

		//*
		JsonRpcServer rpcServer = injector.getInstance(JsonRpcServer.class);
		/*/
		WebDocumentStatisticsServiceImpl service = new WebDocumentStatisticsServiceImpl();
		ObjectMapper mapper = new ObjectMapper();
		JsonRpcServer rpcServer = new JsonRpcServer(mapper, service, WebDocumentStatisticsService.class);
		//*/
		WebServiceHandler rpcHandler = new WebServiceHandler(rpcServer);
		ContextHandler webservice = new ContextHandler();
		webservice.setContextPath("/api");
		webservice.setHandler(rpcHandler);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		//contexts.addHandler(webservice);
		contexts.addHandler(new DefaultHandler());

		server.setHandler(contexts);
		server.start();
		server.join();
		if (log.isInfoEnabled()) {
			log.info("Server ended");
		}
	}
}

