package com.rudolfschmidt.alkun.handlers;

import com.rudolfschmidt.alkun.constants.HttpHeader;
import com.rudolfschmidt.alkun.constants.HttpStatus;
import com.rudolfschmidt.alkun.constants.MediaType;
import com.sun.net.httpserver.HttpExchange;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class StaticFileProcessHandler implements ProcessHandler {

	private final HttpExchange httpExchange;

	StaticFileProcessHandler(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	@Override
	public boolean handle(String staticFileFolder) throws Exception {

		final String normalizedPath = httpExchange.getRequestURI().getPath().replaceFirst(staticFileFolder, "");
		final Path filePath = Paths.get(staticFileFolder, normalizedPath);

		if (!Files.isRegularFile(filePath)) {
			return true;
		}

		final String fileName = filePath.getFileName().toString();
		final String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

		final MediaType mediaType = MediaType.findBySuffix(fileSuffix);
		if (mediaType != null) {
			final String contentType = mediaType.getType().concat(HttpHeader.CHARTSET_UTF8);
			httpExchange.getResponseHeaders().set(HttpHeader.CONTENT_TYPE, contentType);
		}

		httpExchange.sendResponseHeaders(HttpStatus.OK_200.code(), Files.size(filePath));
		Files.copy(filePath, httpExchange.getResponseBody());

		return false;
	}
}
