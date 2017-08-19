package com.github.yin.html;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import java.util.Map;

@AutoValue
public abstract class TextStatictics {
	@JsonCreator
	public static TextStatictics create(@JsonProperty("wordFrequncy") Map<String, Long> wordFrequency,
										@JsonProperty("longestWord") String longestWord,
										@JsonProperty("mostUsedChar") char mostUsedChar) {
		return new AutoValue_TextStatictics(wordFrequency, longestWord, mostUsedChar);
	}

	@JsonProperty("wordFrequncy")
	public abstract Map<String, Long> wordFrequency();

	@JsonProperty("longestWord")
	public abstract String longestWord();

	@JsonProperty("mostUsedChar")
	public abstract char mostUsedChar();
}
