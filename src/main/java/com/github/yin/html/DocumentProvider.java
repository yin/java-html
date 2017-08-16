package com.github.yin.html;

import java.io.IOException;

/**
 * Provides document text for further processing in the pipeline.
 */
public interface DocumentProvider {
	String extract() throws IOException;
}
