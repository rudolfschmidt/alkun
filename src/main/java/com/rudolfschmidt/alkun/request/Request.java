package com.rudolfschmidt.alkun.request;

import com.rudolfschmidt.alkun.http.HttpMethod;
import com.rudolfschmidt.alkun.server.PathTokens;
import com.rudolfschmidt.alkun.server.PathTokenizer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static com.rudolfschmidt.alkun.transformers.ResponseTransformers.toBytes;

public class Request {

	private final HttpExchange httpExchange;
	private final String configuredPath;

	private Map<String, String> parameters;
	private byte[] bodyBytes;

	public Request(
			final HttpExchange httpExchange,
			final String configuredPath
	) {
		this.httpExchange = httpExchange;
		this.configuredPath = configuredPath;
	}

	public Optional<String> param(String identifier) throws UnsupportedEncodingException {

		Objects.requireNonNull(configuredPath, "This route has no path provided");

		if (!Optional.ofNullable(parameters).isPresent()) {

			final List<String> requestedTokens = PathTokenizer.tokenizePath(path());
			final List<String> providedTokens = PathTokenizer.tokenizePath(configuredPath);
			final Map<String, String> map = new HashMap<>();

			for (int i = 0; (i < requestedTokens.size()) && (i < providedTokens.size()); i++) {

				final String provided = providedTokens.get(i);
				final String requested = requestedTokens.get(i);

				if (provided.startsWith(PathTokens.PARAM_DELIMITER)) {
					map.put(provided, URLDecoder.decode(requested, StandardCharsets.UTF_8.name()));
				}
			}

			parameters = Collections.unmodifiableMap(map);

		}

		final String normalized = identifier.startsWith(":") ? identifier : ":".concat(identifier);
		final String value = parameters.get(normalized);

		return Optional.ofNullable(value);

	}

	public Optional<String> query(String identifier) {

		return Optional.ofNullable(httpExchange.getRequestURI().getQuery())
				.map(query -> Stream.of(query.split("&")))
				.orElseGet(Stream::empty)
				.map(str -> str.split("="))
				.filter(arr -> arr[0].equals(identifier))
				.findAny()
				.map(arr -> arr[1]);

	}

	public byte[] body() throws IOException {

		if (!Optional.ofNullable(bodyBytes).isPresent()) {

			try (final InputStream in = httpExchange.getRequestBody()) {

				bodyBytes = toBytes(in);

			}

		}

		return bodyBytes;

	}

	public Object attribute(String attributeKey) {
		return httpExchange.getAttribute(attributeKey);
	}

	public <T> T attribute(Class<T> attributeClass) {
		return attributeClass.cast(httpExchange.getAttribute(attributeClass.getSimpleName()));
	}

	public String header(String headerKey) {
		return httpExchange.getRequestHeaders().getFirst(headerKey);
	}

	public HttpMethod method() {
		return HttpMethod.valueOf(httpExchange.getRequestMethod());
	}

	public String protocol() {
		return httpExchange.getProtocol();
	}

	public String path() {
		return httpExchange.getRequestURI().getPath();
	}

}
