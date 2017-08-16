package com.github.yin.html;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticProcessor implements TextProcessor<TextStatictics> {
	private static final Logger log = LoggerFactory.getLogger(StatisticProcessor.class);

	@Override
	public TextStatictics process(String text) {
		Iterable<String> split = Splitter.on(CharMatcher.JAVA_LETTER_OR_DIGIT.negate())
				.omitEmptyStrings().split(text);

		ImmutableList<String> wordList = ImmutableList.copyOf(split);

		Map<String, Long> wordFrequency = extractWordFrequency(wordList);
		Optional<String> maybeLongestWord = extractLongestWord(wordList);
		Integer mostUsedChar = extractMostUsedCharacter(wordList);

		return TextStatictics.create(wordFrequency, maybeLongestWord.orElse(""), (char) mostUsedChar.intValue());
	}

	private Map<String, Long> extractWordFrequency(ImmutableList<String> wordList) {
		return wordList.parallelStream()
					.collect(Collectors.toConcurrentMap(
							(String w) -> w,
							(String w) -> 1L,
							Long::sum));
	}

	private Optional<String> extractLongestWord(ImmutableList<String> wordList) {
		return wordList.parallelStream().reduce((w1, w2) -> w1.length() > w2.length() ? w1 : w2);
	}

	private Integer extractMostUsedCharacter(ImmutableList<String> wordList) {
		ConcurrentMap<Integer, Long> characterStats = wordList.parallelStream()
				.map(w -> w.chars())
				.flatMap(chars -> chars.boxed())
				.collect(Collectors.toConcurrentMap(
						(Integer c) -> c,
						(Integer c) -> 1L,
						Long::sum));
		return Collections.max(characterStats.entrySet(),
				(Map.Entry<Integer, Long> entry1, Map.Entry<Integer, Long> entry2)
						-> Long.compare(entry1.getValue(), entry2.getValue())).getKey();
	}

}
