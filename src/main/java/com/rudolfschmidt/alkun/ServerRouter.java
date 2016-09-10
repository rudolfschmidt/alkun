package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.HttpMethod;
import com.rudolfschmidt.alkun.constants.HttpStatus;
import com.rudolfschmidt.alkun.constants.RequestConstants;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServerRouter implements HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(ServerRouter.class.getName());

	private final Alkun alkun;
	private final List<Router> routers;
	private final ExceptionRoutes exceptions;

	public ServerRouter(Alkun alkun,
						List<Router> routers,
						ExceptionRoutes exceptions) {
		this.alkun = alkun;
		this.routers = routers;
		this.exceptions = exceptions;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			process(exchange);
		} finally {
			exchange.close();
		}
	}

	private void process(HttpExchange exchange) throws IOException {

		List<Router> filtered = routers.stream().filter(router -> {

			String providedPath = router.getPath();
			String requestedPath = exchange.getRequestURI().getPath();

			LOGGER.config("requested route: " + requestedPath + ", provided route: " + providedPath);

			if (providedPath.equals(requestedPath)) {
				return true;
			}

			if (providedPath.endsWith("/*")) {
				return requestedPath.startsWith(providedPath.substring(0, providedPath.length() - 1));
			}

			List<String> providedTokens = LinkTokenizer.tokenize(providedPath, RequestConstants.PATH_TOKEN);
			List<String> requestedTokens = LinkTokenizer.tokenize(requestedPath, RequestConstants.PATH_TOKEN);

			if (providedTokens.size() != requestedTokens.size()) {
				return false;
			}

			for (int i = 0; i < providedTokens.size(); i++) {
				String providedToken = providedTokens.get(i);
				String requestedToken = requestedTokens.get(i);
				if (!providedToken.startsWith(RequestConstants.PATH_PARAM_DELIMITER)
						&& !providedToken.equals(requestedToken)) {
					return false;
				}
			}

			return true;

		}).collect(Collectors.toList());

		if (filtered.isEmpty()) {
			String message = "404 - no matched route found";
			exchange.sendResponseHeaders(HttpStatus.NOT_FOUND_404.status(), message.length());
			exchange.getResponseBody().write(message.getBytes());
			return;
		}

		String requestMethod = exchange.getRequestMethod();
		filtered = filtered.stream()
				.filter(router -> router.getRoutes().stream().anyMatch(methodRoute ->
						filterMethod(methodRoute, requestMethod)))
				.collect(Collectors.toList());

		if (filtered.isEmpty()) {
			exchange.sendResponseHeaders(HttpStatus.METHOD_NOT_ALLOWED_405.status(), 0);
			return;
		}

		for (Router router : filtered) {

			AtomicBoolean next = new AtomicBoolean(false);
			Request request = new Request(exchange, router.getPath());
			Response response = new Response(alkun, exchange, next);

			List<MethodRoute> methodMatched = router.getRoutes().stream()
					.filter(mr -> filterMethod(mr, requestMethod))
					.collect(Collectors.toList());

			for (MethodRoute methodRoute : methodMatched) {

				next.set(false);

				try {
					methodRoute.getRoute().process(request, response);
				} catch (Exception e) {
					execute(exchange, e, request, response
							, methodRoute.getExceptions()
							, router.getExceptions()
							, exceptions);
					return;
				}

				if (!next.get()) {
					break;
				}

			}

			if (!next.get()) {
				break;
			}

		}

		if (exchange.getResponseCode() < 0) {
			String message = "404 - no content sent";
			exchange.sendResponseHeaders(HttpStatus.NOT_FOUND_404.status(), message.length());
			exchange.getResponseBody().write(message.getBytes());
		}
	}

	private void execute(HttpExchange exchange, Exception exception,
						 Request request, Response response,
						 ExceptionRoutes... routes) throws IOException {

		if (routes.length == 0) {
			String message = "500 - Unhandled Exception";
			exchange.sendResponseHeaders(HttpStatus.INTERNAL_SERVER_ERROR_500.status(), message.length());
			exchange.getResponseBody().write(message.getBytes());
		}
		ExceptionRoutes[] next = Arrays.copyOfRange(routes, 1, routes.length);
		if (routes[0].hasInstanceOf(exception)) {
			try {
				routes[0].process(exception, request, response);
			} catch (Exception e) {
				execute(exchange, e, request, response, next);
			}
		} else {
			execute(exchange, exception, request, response, next);
		}
	}

	private boolean filterMethod(MethodRoute methodRoute, String requestedMethod) {
		return methodRoute.getMethod().toString().equals(requestedMethod)
				|| methodRoute.getMethod() == HttpMethod.ANY;
	}
}
