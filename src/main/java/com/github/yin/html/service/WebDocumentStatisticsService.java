package com.github.yin.html.service;

import com.github.yin.html.TextStatictics;

public interface WebDocumentStatisticsService {
	TextStatictics computeStatictics(String url);
}
