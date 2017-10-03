package com.rudolfschmidt.alkun.transformers;

import com.google.gson.Gson;
import com.rudolfschmidt.alkun.constants.HttpHeader;
import com.rudolfschmidt.alkun.constants.MediaType;
import com.sun.net.httpserver.Headers;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ResponseTransformers {

	public static byte[] asBytes(URL url) throws IOException {
		try (
			final InputStream in = url.openStream();
			final ByteArrayOutputStream out = new ByteArrayOutputStream()
		) {
			final byte[] buffer = new byte[1024];
			for (int b = in.read(buffer); b != -1; b = in.read(buffer)) {
				out.write(buffer, 0, b);
			}
			return out.toByteArray();
		}
	}

	public static byte[] asImage(URL url) throws IOException {
		final URLConnection connection = url.openConnection();
		try (
			final InputStream inputStream = connection.getInputStream();
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
		) {
			String contentType;
			contentType = connection.getContentType();
			contentType = contentType.substring(contentType.lastIndexOf("/") + 1);
			ImageIO.write(ImageIO.read(inputStream), contentType, outputStream);
			return outputStream.toByteArray();
		}
	}

	public static byte[] asJson(Object model) {
		return new Gson().toJson(model).getBytes();
	}

	public void asFile(Headers headers, String fileName) {
		headers.set(HttpHeader.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
	}

	public void asType(Headers headers, MediaType mediaType) {
		headers.set(HttpHeader.CONTENT_TYPE, mediaType.getType().concat(HttpHeader.CHARTSET_UTF8));
	}

}
