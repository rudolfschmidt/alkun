package com.rudolfschmidt.alkun.transformers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

public class JsonTransformer {

	private static final Gson GSON;

	static {
		final GsonBuilder builder = new GsonBuilder();
		GSON = builder.create();
	}

	public static String toJson(Object model) {
		return GSON.toJson(model);
	}

	public static String toPrettyJson(Object model) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(model);
	}

	public static String toPrettyJson(String json) {
		return toPrettyJson(new JsonParser().parse(json));
	}

	public static <T> Optional<T> fromJson(Reader json, Class<T> modelClass) {
		return Optional.ofNullable(GSON.fromJson(json, modelClass));
	}

	public static <T> Optional<T> fromJson(byte[] json, Class<T> modelClass) {
		return fromJson(new String(json), modelClass);
	}

	public static <T> Optional<T> fromJson(String json, Class<T> modelClass) {
		return Optional.ofNullable(GSON.fromJson(json, modelClass));
	}

}
