package com.github.yin.html.service;

import com.github.yin.html.DocumentProvider;
import com.github.yin.html.StatisticProcessor;
import com.github.yin.html.TextProcessingTask;
import com.github.yin.html.TextStatictics;
import com.github.yin.html.WebDocumentProvider;
import com.github.yin.html.main.CommandLineMain;
import com.google.inject.*;

import java.util.function.Consumer;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDocumentStatisticsServiceImpl implements WebDocumentStatisticsService {
	private static final Logger log = LoggerFactory.getLogger(CommandLineMain.class);

	@Override
	public TextStatictics computeStatictics(String url) {
		Injector injector = Guice.createInjector(new WebDocumentStatisticsModule(url));
		TextProcessingTask<TextStatictics> task = injector.getInstance(
				Key.get(new TypeLiteral<TextProcessingTask<TextStatictics>>() {}));
		return task.execute();
	}

	static class WebDocumentStatisticsModule extends AbstractModule {
		private final String url;

		public WebDocumentStatisticsModule(String url) {
			this.url = url;
		}

		@Override
		protected void configure() {
			bind(StatisticProcessor.class);
		}

		@Provides
		TextProcessingTask<TextStatictics> createTextProcessingTask(DocumentProvider provider,
				StatisticProcessor processor) {
			return new TextProcessingTask<>(provider, processor);
		}

		@Provides DocumentProvider createDocumentProvider() {
			return new WebDocumentProvider(url);
		}
	}
}
