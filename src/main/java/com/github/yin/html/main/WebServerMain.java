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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

public class WebServerMain {
	private static Logger log = LoggerFactory.getLogger(WebServerMain.class);

	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(
				new JettyHttpServerModule(),
				new JsonRpcServerModule(new StatisticsServiceModule()));

		Server server = injector.getInstance(Server.class);
		JsonRpcServer rpcServer = injector.getInstance(JsonRpcServer.class);

		ContextHandler webapp = new ContextHandler();
		webapp.setContextPath("/");
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setBaseResource(Resource.newClassPathResource("index.html"));
		webapp.setHandler(resourceHandler);

		WebServiceHandler rpcHandler = new WebServiceHandler(rpcServer);
		ContextHandler webservice = new ContextHandler("/api");
		webservice.setHandler(rpcHandler);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.addHandler(webapp);
		contexts.addHandler(webservice);
		server.setHandler(new HandlerList(contexts, new DefaultHandler()));

		try {
			server.start();
		} finally {
			server.join();
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

