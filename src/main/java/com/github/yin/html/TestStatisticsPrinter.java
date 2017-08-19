package com.github.yin.html;

import java.io.PrintStream;
import java.util.Map;
import java.util.function.Consumer;

public class TestStatisticsPrinter implements Consumer<TextStatictics> {
	private final PrintStream out;

	public TestStatisticsPrinter(PrintStream out) {
		this.out = out;
	}

	@Override
	public void accept(TextStatictics stats) {
		out.printf("Word frequencies:\n");
		for (Map.Entry entry : stats.wordFrequency().entrySet()) {
			out.printf("\t%s => %d\n", entry.getKey(), entry.getValue());
		}
		out.printf("Longest word: %s\n", stats.longestWord());
		out.printf("Most used character: %c\n", stats.mostUsedChar());

	}
}
