package com.github.yin.html.main;

import com.github.yin.html.jetty.WebServiceHandler;
import com.github.yin.html.modules.JettyHttpServerModule;
import com.github.yin.html.modules.JsonRpcServerModule;
import com.github.yin.html.service.WebDocumentStatisticsService;
import com.github.yin.html.service.WebDocumentStatisticsServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebServerMain {
	private static Logger log = LoggerFactory.getLogger(WebServerMain.class);

	public static void main(String[] args) throws Exception {
		//*
		Injector injector = Guice.createInjector(
				new JettyHttpServerModule(),
				new JsonRpcServerModule(new StatisticsServiceModule()));
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


		//*
		JsonRpcServer rpcServer = injector.getInstance(JsonRpcServer.class);

		ContextHandler webapp = new ContextHandler();
		webapp.setContextPath("/");
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setBaseResource(Resource.newClassPathResource("index.html"));
		webapp.setHandler(resourceHandler);
		/*/
		WebDocumentStatisticsServiceImpl service = new WebDocumentStatisticsServiceImpl();
		ObjectMapper mapper = new ObjectMapper();
		JsonRpcServer rpcServer = new JsonRpcServer(mapper, service, WebDocumentStatisticsService.class);
		//*/

		WebServiceHandler rpcHandler = new WebServiceHandler(rpcServer);
		ContextHandler webservice = new ContextHandler("/api");
		webservice.setHandler(rpcHandler);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.addHandler(webapp);
		contexts.addHandler(webservice);
		//*

		server.setHandler(new HandlerList(contexts, new DefaultHandler()));
		/*/
		server.setHandler(new HandlerList(contexts, webservice, new DefaultHandler()));
		//*/
		server.start();
		server.join();
		if (log.isInfoEnabled()) {
			log.info("Server ended");
		}
	}

	private static class StatisticsServiceModule extends AbstractModule {
		@Override
        protected void configure() {
			log.info("stats rpc config");

			bind(Class.class).annotatedWith(JsonRpcServerModule.RpcInterface.class)
                    .toInstance(WebDocumentStatisticsService.class);
            bind(Object.class).annotatedWith(JsonRpcServerModule.RpcService.class)
                    .to(WebDocumentStatisticsServiceImpl.class);
        }

        @Provides @Singleton
		WebDocumentStatisticsServiceImpl createWebDocumentStatisticsServiceImpl() {
			return new WebDocumentStatisticsServiceImpl();
		}
	}
}

