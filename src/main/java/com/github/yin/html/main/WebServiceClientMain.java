package com.github.yin.html.main;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yin.flags.Flag;
import com.github.yin.flags.Flags;
import com.github.yin.flags.annotations.FlagDesc;
import com.github.yin.html.TestStatisticsPrinter;
import com.github.yin.html.TextStatictics;
import com.github.yin.html.modules.JacksonModule;
import com.github.yin.html.service.WebDocumentStatisticsService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Invokes remote WebService for text statistics processing for each positional argument.
 */
@FlagDesc
public class WebServiceClientMain implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(WebServiceClientMain.class);

	@FlagDesc("URL of the server API to call remotely. Default: http://localhost:8080/")
	private static final Flag<String> apiUrl = Flags.create("http://localhost:8080/api");
	private final String serverApiUrl;
	private final String documentUrl;

	public WebServiceClientMain(String serverApiUrl, String documentUrl) {
		this.serverApiUrl = serverApiUrl;
		this.documentUrl = documentUrl;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		List<String> urls = Flags.parse(args, ImmutableList.of("com.github.yin.html.main"));
		ExecutorService executor = Executors.newSingleThreadExecutor();
		for (String documentUrl : urls) {
			try {
				String serverApiUrl = apiUrl.get();
				if (!serverApiUrl.endsWith("/")) {
					serverApiUrl = serverApiUrl.concat("/");
				}
				executor.execute(new WebServiceClientMain(serverApiUrl, documentUrl));
			} catch(Throwable t) {
				log.error("Error getting response from server", t);
			}
		}
		executor.shutdown();
	}

	@Override
	public void run() {
		try {
			Injector injector = Guice.createInjector(new ClientModule(serverApiUrl));
			WebDocumentStatisticsService client = injector.getInstance(WebDocumentStatisticsService.class);

			log.info("Making request");
			TextStatictics response = client.computeStatictics(documentUrl);
			TestStatisticsPrinter printer = new TestStatisticsPrinter(System.out);
			log.info("Response received");

			printer.accept(response);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	class ClientModule extends AbstractModule {
		private final URL url;

		public ClientModule(String url) throws MalformedURLException {
			this.url = new URL(checkNotNull(url));
		}

		@Override
		protected void configure() {
			install(new JacksonModule());
		}

		@Provides
		public JsonRpcHttpClient createJsonRpcHttpClient(ObjectMapper mapper) throws MalformedURLException {
			return new JsonRpcHttpClient(mapper, url, ImmutableMap.of());
		}

		@Provides
		public WebDocumentStatisticsService createItemQueryClient(JsonRpcHttpClient client) {
			WebDocumentStatisticsService proxy = ProxyUtil.createClientProxy(getClass().getClassLoader(),
					WebDocumentStatisticsService.class, client);
			return proxy;
		}
	}
}