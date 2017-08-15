package com.github.yin.html;

import com.github.yin.flags.Flag;
import com.github.yin.flags.Flags;
import com.github.yin.flags.annotations.FlagDesc;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebTextExtractor implements Runnable {
	@FlagDesc("Print only text-statistics. Default: true")
	static final Flag<Boolean> stats = Flags.create(true);

	@FlagDesc("Replace tags with alt value, which have the attribute. Default: false")
	static final Flag<Boolean> alts = Flags.create(false);

	private static final Logger log = LoggerFactory.getLogger(WebTextExtractor.class);

	private final String url;

	public WebTextExtractor(String url) {
		this.url = url;
	}

	public static void main(String[] args) {
		List<String> positional = Flags.parse(args, ImmutableList.of("com.github.yin.html"));

		ExecutorService executorService = Executors.newSingleThreadExecutor();

		for (String url : positional) {
			executorService.execute(new WebTextExtractor(url));
		}
		executorService.shutdown();
	}

	@Override
	public void run() {
		try {
			Document doc = Jsoup.connect(url).followRedirects(true).get();
			String text = doc.body().text();
			Iterable<String> split = Splitter.on(CharMatcher.JAVA_LETTER_OR_DIGIT.negate())
					.omitEmptyStrings().split(text);

			ImmutableList<String> wordList = ImmutableList.copyOf(split);
			Map<String, Long> wordCounts = wordList.parallelStream()
					.collect(Collectors.toConcurrentMap(
							(String w) -> w,
							(String w) -> 1L,
							Long::sum));

			Optional<String> maybeLongestWord = wordList.parallelStream().reduce((w1, w2) -> w1.length() > w2.length() ? w1 : w2);

			ConcurrentMap<Integer, Long> characterStats = wordList.parallelStream()
					.map(w -> w.chars())
					.flatMap(chars -> chars.boxed())
					.collect(Collectors.toConcurrentMap(
							(Integer c) -> c,
							(Integer c) -> 1L,
							Long::sum));
			Integer maxCharCode = Collections.max(characterStats.entrySet(),
					(Map.Entry<Integer, Long> entry1, Map.Entry<Integer, Long> entry2)
							-> Long.compare(entry1.getValue(), entry2.getValue())).getKey();

			log.info("Word counts:");
			for (Map.Entry wordStat : wordCounts.entrySet()) {
				log.info("\t{} => {}", wordStat.getKey(), wordStat.getValue());
			}
			log.info("Longest word: {}", maybeLongestWord);
			log.info("Most used character: {}", (char) maxCharCode.intValue());

		} catch (IOException ex) {
			log.error("Could not process url: " + url, ex);
		}
	}
}
