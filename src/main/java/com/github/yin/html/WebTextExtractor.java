package com.github.yin.html;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebTextExtractor {

	public static void main(String[] args) throws IOException {

		Document doc = Jsoup.connect(args[0]).get();
	}
}
