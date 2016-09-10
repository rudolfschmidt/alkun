package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.HttpHeader;
import com.rudolfschmidt.alkun.constants.HttpStatus;
import com.rudolfschmidt.alkun.constants.MediaType;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Response {

	private final Alkun alkun;
	private final HttpExchange exchange;
	private final AtomicBoolean next;

	protected Response(Alkun alkun, HttpExchange exchange, AtomicBoolean next) {
		this.alkun = alkun;
		this.exchange = exchange;
		this.next = next;
	}

	public void end() throws IOException {
		status(HttpStatus.OK_200);
	}

	public void send(String data) throws IOException {
		defaultContentType(MediaType.TEXT_PLAIN);
		write(data);
	}

	public void send(String data, HttpStatus httpStatus) throws IOException {
		defaultContentType(MediaType.TEXT_PLAIN);
		write(data, httpStatus);
	}

	public void json(String json) throws IOException {
		defaultContentType(MediaType.APPLICATION_JSON);
		write(json);
	}

	public void json(String json, HttpStatus httpStatus) throws IOException {
		defaultContentType(MediaType.APPLICATION_JSON);
		write(json, httpStatus);
	}

	public void render(String template) throws IOException {
		defaultContentType(MediaType.TEXT_HTML);
		write(alkun.engine().apply(template, Optional.empty()));
	}

	public void render(String template, Object model) throws IOException {
		defaultContentType(MediaType.TEXT_HTML);
		write(alkun.engine().apply(template, Optional.ofNullable(model)));
	}

	public void send(URL url) throws IOException {
		try (
				InputStream in = url.openStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream()
		) {
			while (true) {
				int read = in.read();
				if (read < 0) {
					break;
				}
				out.write(read);
			}
			exchange.sendResponseHeaders(HttpStatus.OK_200.status(), out.size());
			exchange.getResponseBody().write(out.toByteArray());
		}
	}

	public void sendImage(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		String contentType = connection.getContentType();
		contentType = contentType.substring(contentType.lastIndexOf("/") + 1);
		try (
				InputStream inputStream = connection.getInputStream();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
		) {
			BufferedImage img = ImageIO.read(inputStream);
			ImageIO.write(img, contentType, outputStream);
			exchange.sendResponseHeaders(HttpStatus.OK_200.status(), outputStream.size());
			ImageIO.write(img, contentType, exchange.getResponseBody());
		}
	}

	public void sendFile(Path filePath) throws IOException {
		String fileName = filePath.getFileName().toString();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		MediaType.findBySuffix(suffix).ifPresent(this::defaultContentType);
		exchange.sendResponseHeaders(HttpStatus.OK_200.status(), Files.size(filePath));
		Files.copy(filePath, exchange.getResponseBody());
	}

	public void sendTextFile(String text, String fileName) throws IOException {
		header(HttpHeader.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		write(text);
	}

	public void status(HttpStatus httpStatus) throws IOException {
		exchange.sendResponseHeaders(httpStatus.status(), 0);
	}

	public void forward(String location) throws IOException {
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set(HttpHeader.LOCATION, location);
		status(HttpStatus.FOUND_302);
	}

	public void redirect(String location) throws IOException {
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set(HttpHeader.LOCATION, location);
		status(HttpStatus.TEMPORARY_REDIRECT_307);
	}

	public void type(MediaType mediaType) {
		type(mediaType.getType());
	}

	public void type(String mediaType) {
		mediaType = mediaType + HttpHeader.CHARTSET_UTF8;
		header(HttpHeader.CONTENT_TYPE, mediaType);
	}

	public CookieBuilder cookie(String key, String value) {
		return new CookieBuilder(this, key, value);
	}

	public void header(String name, String value) {
		exchange.getResponseHeaders().set(name, value);
	}

	public void attribute(String key, String value) {
		exchange.setAttribute(key, value);
	}

	/**
	 * Multiple routes can be matched by the same url.
	 * Execution of this method leads execute the next matched route after finishing the first route.
	 */
	public void next() {
		next.set(true);
	}

	private void write(String data) throws IOException {
		write(data, HttpStatus.OK_200);
	}

	private void write(String data, HttpStatus httpStatus) throws IOException {
		byte[] response = data.getBytes();
		exchange.sendResponseHeaders(httpStatus.status(), response.length);
		exchange.getResponseBody().write(response);
	}

	private void defaultContentType(MediaType mediaType) {
		Headers headers = exchange.getResponseHeaders();
		if (headers.containsKey(HttpHeader.CONTENT_TYPE)) {
			return;
		}
		type(mediaType);
	}


}
