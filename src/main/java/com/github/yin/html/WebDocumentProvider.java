package com.github.yin.html;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Extracts text from HTML document provided by an URL.
 */
public class WebDocumentProvider implements DocumentProvider {

	private final String url;

	public WebDocumentProvider(String url) {
		this.url = url;
	}

	@Override
	public String extract() throws IOException {
		Document doc = Jsoup.connect(url).followRedirects(true).get();
		String text = doc.body().text();
		return text;
	}

	public String getUrl() {
		return url;
	}
}
