package com.github.yin.html;

import java.io.IOException;

public class TextProcessingTask<R> implements ProcessingTask<R> {
	private final DocumentProvider extractor;
	private final TextProcessor<R> processor;

	public TextProcessingTask(DocumentProvider extractor, TextProcessor<R> processor) {
		this.extractor = extractor;
		this.processor = processor;
	}

	@Override
	public R execute() {
		try {
			String text = extractor.extract();
			R result = processor.process(text);
			return result;
		} catch (IOException ex) {
			throw new ProcessingRuntimeException();
		}
	}

}
