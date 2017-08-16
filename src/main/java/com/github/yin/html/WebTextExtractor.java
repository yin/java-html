package com.github.yin.html;

import com.github.yin.flags.Flag;
import com.github.yin.flags.Flags;
import com.github.yin.flags.annotations.FlagDesc;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebTextExtractor implements Runnable {
	@FlagDesc("Print only text-statistics. Default: true")
	static final Flag<Boolean> stats = Flags.create(true);

	@FlagDesc("Replace tags with alt value, which have the attribute. Default: false")
	static final Flag<Boolean> alts = Flags.create(false);

	private static final Logger log = LoggerFactory.getLogger(WebTextExtractor.class);
	private final WebDocumentProvider extractor;
	private final TextProcessor processor;

	public WebTextExtractor(WebDocumentProvider extractor, TextProcessor processor) {
		this.extractor = extractor;
		this.processor = processor;
	}

	public static void main(String[] args) {
		List<String> positional = Flags.parse(args, ImmutableList.of("com.github.yin.html"));

		ExecutorService executorService = Executors.newSingleThreadExecutor();

		for (String url : positional) {
			executorService.execute(new WebTextExtractor(new WebDocumentProvider(url), new StatisticProcessor()));
		}
		executorService.shutdown();
	}

	@Override
	public void run() {
		try {
			processor.process(extractor.extract());
		} catch (IOException ex) {
			log.error("Could not process url: " + extractor.getUrl(), ex);
		}
	}
}
