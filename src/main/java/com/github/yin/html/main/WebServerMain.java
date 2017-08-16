package com.github.yin.html.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yin.html.jetty.WebServiceHandler;
import com.googlecode.jsonrpc4j.JsonRpcServer;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;

public class WebServerMain {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		/*
		ContextHandler webapp = new ContextHandler();
		webapp.setContextPath("/");
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setBaseResource(Resource.newClassPathResource("index.html"));
		webapp.setHandler(resourceHandler);
		*/
/*
		ObjectMapper mapper = new ObjectMapper().registerModules();
		JsonRpcServer rpcServer = new JsonRpcServer(mapper, service, iface);

		WebServiceHandler rpcHandler = new WebServiceHandler(rpcServer);
*/
		ContextHandler webservice = new ContextHandler();
		webservice.setContextPath("/");
//		webservice.setHandler(rpcHandler);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { webservice, new DefaultHandler() });

		server.setHandler(contexts);

		server.start();
		server.dumpStdErr();
		server.join();
	}
}

