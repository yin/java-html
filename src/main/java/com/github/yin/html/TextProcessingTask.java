package com.github.yin.html;

import java.io.IOException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextProcessingTask<R> implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(CommandLineMain.class);
	private final DocumentProvider extractor;
	private final TextProcessor<R> processor;
	private final Consumer<R> formatter;

	public TextProcessingTask(DocumentProvider extractor, TextProcessor<R> processor,
			Consumer<R> formatter) {
		this.extractor = extractor;
		this.processor = processor;
		this.formatter = formatter;
	}

	@Override
	public void run() {
		try {
			String text = extractor.extract();
			R result = processor.process(text);
			formatter.accept(result);
		} catch (IOException ex) {
			log.error("Could not process document", ex);
		}
	}

}
