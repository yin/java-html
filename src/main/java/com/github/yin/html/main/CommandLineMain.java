package com.github.yin.html.main;

import com.github.yin.flags.Flag;
import com.github.yin.flags.Flags;
import com.github.yin.flags.annotations.FlagDesc;
import com.github.yin.html.StatisticProcessor;
import com.github.yin.html.TextProcessingTask;
import com.github.yin.html.TextStatictics;
import com.github.yin.html.WebDocumentProvider;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineMain {
	@FlagDesc("Print only text-statistics. Default: true")
	static final Flag<Boolean> stats = Flags.create(true);

	@FlagDesc("Replace tags with alt value, which have the attribute. Default: false")
	static final Flag<Boolean> alts = Flags.create(false);

	private static final Logger log = LoggerFactory.getLogger(CommandLineMain.class);

	public static void main(String[] args) {
		List<String> positional = Flags.parse(args, ImmutableList.of("com.github.yin.html"));

		ExecutorService executorService = Executors.newSingleThreadExecutor();

		for (String url : positional) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					TextStatictics stats = new TextProcessingTask<>(new WebDocumentProvider(url),
							new StatisticProcessor()).execute();
					log.info("URL: {}", url);
					log.info("Word counts:");
					for (Map.Entry entry : stats.wordFrequency().entrySet()) {
						log.info("\t{} => {}", entry.getKey(), entry.getValue());
					}
					log.info("Longest word: {}", stats.longestWord());
					log.info("Most used character: {}", stats.mostUsedChar());
				}
			});
		}
		executorService.shutdown();
	}

}
