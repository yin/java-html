package com.github.yin.html;

import com.google.auto.value.AutoValue;

import java.util.Map;

@AutoValue
public abstract class TextStatictics {
	public static TextStatictics create(Map<String, Long> wordFrequency, String longestWord, char mostUsedChar) {
		return new AutoValue_TextStatictics(wordFrequency, longestWord, mostUsedChar);
	}

	public abstract Map<String, Long> wordFrequency();

	public abstract String longestWord();

	public abstract char mostUsedChar();
}
