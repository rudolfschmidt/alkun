package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.RequestConstants;
import com.rudolfschmidt.alkun.handlers.MultiPartEncoder;
import com.rudolfschmidt.alkun.constants.HttpHeader;
import com.rudolfschmidt.alkun.handlers.Part;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Request {

	private final HttpExchange exchange;
	private final String path;

	private String raw;

	protected Request(HttpExchange exchange, String path) {
		this.exchange = exchange;
		this.path = path;
	}

	public Optional<String> params(String identifier) {
		List<String> providedTokens = LinkTokenizer.tokenize(path, RequestConstants.PATH_TOKEN);
		List<String> requestedTokens = LinkTokenizer.tokenize(path(), RequestConstants.PATH_TOKEN);
		for (int i = 0; i < providedTokens.size(); i++) {
			String providedToken = providedTokens.get(i);
			String requestedToken = requestedTokens.get(i);
			if (providedToken.startsWith(RequestConstants.PATH_PARAM_DELIMITER)
					&& providedToken.endsWith(identifier)) {
				return Optional.ofNullable(requestedToken);
			}
		}
		return Optional.empty();
	}

	public URI uri() {
		return exchange.getRequestURI();
	}

	public String path() {
		return uri().getPath();
	}

	public String method() {
		return exchange.getRequestMethod();
	}

	public String basePath() {
		return path.split(RequestConstants.PATH_PARAM_DELIMITER)[0];
	}

	public String body() throws IOException {
		if (raw == null) {
			try (InputStream in = exchange.getRequestBody()) {
				StringBuilder sb = new StringBuilder();
				int c;
				while ((c = in.read()) != -1) {
					sb.append((char) c);
				}
				raw = URLDecoder.decode(sb.toString(), StandardCharsets.UTF_8.name());
			}
		}
		return raw;
	}

	public Optional<String> body(String key) throws IOException {
		String query = body();
		for (String token : query.split("&")) {
			String[] arr = token.split("=", 2);
			if (key.equals(arr[0])) {
				return Optional.ofNullable(arr[1]);
			}
		}
		return Optional.empty();
	}

	public String queries() {
		return Optional.ofNullable(uri().getQuery()).orElse("");
	}

	public Optional<String> queries(String value) {
		return Optional.ofNullable(value)
				.flatMap(val -> Optional.ofNullable(uri().getQuery())
						.map(query -> query.split("&"))
						.flatMap(tokens -> Stream.of(tokens)
								.map(token -> token.split("="))
								.filter(arr -> arr.length > 1)
								.filter(arr -> arr[0].equals(val))
								.map(arr -> arr[1])
								.findAny()
						))
				.filter(str -> !str.isEmpty());

	}

	public Headers headers() {
		return exchange.getRequestHeaders();
	}

	public Object attribute(String key) {
		return exchange.getAttribute(key);
	}

	public <T> T attribute(Class<T> attribute) {
		return attribute.cast(exchange.getAttribute(attribute.getSimpleName()));
	}

	public List<Part> multipart() throws IOException {
		List<Part> parts = new ArrayList<>();
		if (!MultiPartEncoder.isMultiPart(headers())) {
			return parts;
		}
		MultiPartEncoder.encode(exchange, parts);
		return parts;
	}

	public Optional<String> cookie(String cookieName) {
		return Optional.ofNullable(exchange)
				.map(HttpExchange::getRequestHeaders)
				.map(requestHeaders -> requestHeaders.getFirst(HttpHeader.GET_COOKIE))
				.flatMap(cookies -> Optional.ofNullable(cookieName).flatMap(name ->
						Stream.of(cookies.split("; "))
								.map(token -> token.split("="))
								.filter(tokens -> tokens.length > 1)
								.filter(tokens -> name.equals(tokens[0]))
								.map(tokens -> tokens[1])
								.findAny()
								.filter(str -> !str.isEmpty())
				));
	}


}
