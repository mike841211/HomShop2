package com.homlin.utils;

import java.nio.charset.Charset;

import org.springframework.http.converter.StringHttpMessageConverter;

public class UTF8StringHttpMessageConverter extends StringHttpMessageConverter {
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public UTF8StringHttpMessageConverter() {
		super(DEFAULT_CHARSET);
	}
}
