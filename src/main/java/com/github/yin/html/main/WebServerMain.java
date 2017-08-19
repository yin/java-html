package com.github.yin.html.main;

import com.github.yin.flags.Flag;
import com.github.yin.flags.Flags;
import com.github.yin.flags.annotations.FlagDesc;
import com.github.yin.html.jetty.WebServiceHandler;
import com.github.yin.html.modules.JettyHttpServerModule;
import com.github.yin.html.modules.JsonRpcServerModule;
import com.github.yin.html.service.WebDocumentStatisticsService;
import com.github.yin.html.service.WebDocumentStatisticsServiceImpl;
import com.google.common.collect.ImmutableList;
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

@FlagDesc
public class WebServerMain {
	@FlagDesc("Server port to listen on")
	private static final Flag<Integer> port = Flags.create(8080);

	private static Logger log = LoggerFactory.getLogger(WebServerMain.class);

	public static void main(String[] args) throws Exception {
		Flags.parse(args, ImmutableList.of("com.github.yin.html.main"));
		Injector injector = Guice.createInjector(
				new JettyHttpServerModule(port.get()),
				new JsonRpcServerModule(new StatisticsServiceModule()));

		Server server = injector.getInstance(Server.class);
		JsonRpcServer rpcServer = injector.getInstance(JsonRpcServer.class);

		ContextHandler webapp = createContextResource("/", Resource.newClassPathResource("index.html"));

		ContextHandler file1 = createContextResource("/ajax-loader.gif/", Resource.newClassPathResource("ajax-loader.gif"));

		WebServiceHandler rpcHandler = new WebServiceHandler(rpcServer);
		ContextHandler webservice = new ContextHandler("/api");
		webservice.setHandler(rpcHandler);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.addHandler(webapp);
		contexts.addHandler(file1);
		contexts.addHandler(webservice);
		server.setHandler(new HandlerList(contexts, new DefaultHandler()));

		try {
			server.start();
		} finally {
			server.join();
			log.info("Server ended");
		}
	}

	private static ContextHandler createContextResource(String contextPath, Resource base) {
		ContextHandler file1 = new ContextHandler(contextPath);
		{
			ResourceHandler resourceHandler = new ResourceHandler();
			resourceHandler.setBaseResource(base);
			file1.setHandler(resourceHandler);
		}
		return file1;
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

