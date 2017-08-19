package com.github.yin.html.jetty;

import com.googlecode.jsonrpc4j.JsonRpcServer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forwards HTTP requests from Jetty jetty to jsonrpc4j for handling RPCs.
 */
final public class WebServiceHandler extends AbstractHandler {
	private static final Logger log = LoggerFactory.getLogger(WebServiceHandler.class);
	private final JsonRpcServer jsonrpc;

	public WebServiceHandler(JsonRpcServer jsonrpc) {
		if (log.isDebugEnabled()) {
			System.out.println("Jetty->Jsonrpc handler created");
		}
		this.jsonrpc = jsonrpc;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("received handle http request({}->{})@{}", request.getRemoteHost(), request.getLocalAddr(), request.hashCode());
			}
			jsonrpc.handle(baseRequest , response);
			baseRequest.setHandled(true);
			if (log.isDebugEnabled()) {
				log.debug("handled http request({}->{})@{}", request.getRemoteHost(), request.getLocalAddr(), request.hashCode());
			}
		} catch(Throwable t) {
			log.error("failed to handle http request", t);
		}
	}
}
