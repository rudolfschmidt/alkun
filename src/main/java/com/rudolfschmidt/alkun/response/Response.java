package com.rudolfschmidt.alkun.response;

import com.rudolfschmidt.alkun.http.HttpHeader;
import com.rudolfschmidt.alkun.http.HttpStatus;
import com.rudolfschmidt.alkun.http.MediaType;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Response {

	private final HttpExchange httpExchange;

	public Response(
			final HttpExchange httpExchange
	) {
		this.httpExchange = httpExchange;
	}

	public void attribute(String key, String value) {

		key = Objects.requireNonNull(key);
		value = Objects.requireNonNull(value);

		httpExchange.setAttribute(key, value);
	}

	public void attribute(Object attribute) {

		attribute = Objects.requireNonNull(attribute);

		httpExchange.setAttribute(attribute.getClass().getSimpleName(), attribute);

	}

	public Result status(HttpStatus httpStatus, byte[] data) throws IOException {

		httpStatus = Objects.requireNonNull(httpStatus);
		data = Objects.requireNonNull(data);

		httpExchange.sendResponseHeaders(httpStatus.code(), data.length);
		httpExchange.getResponseBody().write(data);

		return new Result();

	}

	public Result status(HttpStatus httpStatus) throws IOException {

		httpStatus = Objects.requireNonNull(httpStatus);

		return status(httpStatus, httpStatus.getReasonPhrase().getBytes());

	}

	public Result status(HttpStatus httpStatus, String data) throws IOException {

		httpStatus = Objects.requireNonNull(httpStatus);
		data = Objects.requireNonNull(data);

		return status(httpStatus, data.getBytes());

	}

	public Result file(HttpStatus httpStatus, Path filePath) throws IOException {

		httpStatus = Objects.requireNonNull(httpStatus);
		filePath = Objects.requireNonNull(filePath);

		final String fileName = filePath.getFileName().toString();
		final String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

		MediaType.findBySuffix(fileSuffix).ifPresent(mediaType ->
				header(HttpHeader.CONTENT_TYPE, mediaType.getType().concat(HttpHeader.CHARTSET_UTF8)));

		httpExchange.sendResponseHeaders(httpStatus.code(), Files.size(filePath));
		Files.copy(filePath, httpExchange.getResponseBody());

		return new Result();

	}

	public Result redirect(String location) throws IOException {
		header(HttpHeader.LOCATION, location);
		return status(HttpStatus.MOVED_TEMPORARILY_302);
	}

	public Result next() {
		return new Result(true);
	}

	public Headers headers() {
		return httpExchange.getResponseHeaders();
	}

	public void header(String key, String value) {
		headers().set(Objects.requireNonNull(key), Objects.requireNonNull(value));
	}

}
