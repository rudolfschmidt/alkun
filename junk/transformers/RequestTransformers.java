package com.rudolfschmidt.alkun.transformers;

import com.google.gson.Gson;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class RequestTransformers {

	public static JsonReader fromJson(byte[] bodyBytes) {
		return Json.createReader(new ByteArrayInputStream(bodyBytes));
	}

	public static <T> T fromJson(byte[] bodyBytes, Class<T> modelClass) {
		return new Gson().fromJson(new StringReader(new String(bodyBytes)), modelClass);
	}

	public static String fromUrlEncoded(byte[] bodyBytes) throws UnsupportedEncodingException {
		return URLDecoder.decode(new String(bodyBytes), StandardCharsets.UTF_8.name());
	}

	public static String fromUrlEncoded(byte[] bodyBytes, String identifier) throws UnsupportedEncodingException {
		for (String token : fromUrlEncoded(bodyBytes).split("&")) {
			final String[] arr = token.split("=", 2);
			if (identifier.equals(arr[0])) {
				return arr[1];
			}
		}
		throw new NoSuchElementException();
	}

}
