package com.rudolfschmidt.alkun.http.encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Encoder {
	public static String encode(String str) {
		try {
			return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new EncodingException();
		}
	}
	public static String decode(String str) {
		try {
			return URLDecoder.decode(str, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new EncodingException();
		}
	}
}
