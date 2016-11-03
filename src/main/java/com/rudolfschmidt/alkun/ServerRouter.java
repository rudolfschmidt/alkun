package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.HttpStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerRouter implements HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(ServerRouter.class.getName());

	private final Alkun alkun;

	public ServerRouter(Alkun alkun) {
		this.alkun = alkun;
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

		String requestedPath = exchange.getRequestURI().getPath();
		List<Router> routers = alkun.routers().stream()
				.peek(router -> LOGGER.config("[PRE_FILTER]:[" + requestedPath + "]:" + router))
				.filter(router -> Stream.of(router.paths())
						.anyMatch(providedPath -> Filters.filterPath(providedPath, requestedPath))
				).collect(Collectors.toList());

		if (routers.isEmpty()) {
			exchange.sendResponseHeaders(HttpStatus.NOT_FOUND_404.status(), 0);
			return;
		}

		String requestMethod = exchange.getRequestMethod();
		if (routers.stream().noneMatch(router -> router.routes().stream()
				.anyMatch(route -> filterMethod(route, requestMethod)))) {
			exchange.sendResponseHeaders(HttpStatus.METHOD_NOT_ALLOWED_405.status(), 0);
			return;
		}

		for (Router router : routers) {

			for (String path : Stream.of(router.paths())
					.filter(providedPath -> Filters.filterPath(providedPath, requestedPath))
					.collect(Collectors.toList())) {

				AtomicBoolean next = new AtomicBoolean(false);
				Request request = new Request(exchange, path);
				Response response = new Response(alkun, exchange, next);

				for (MethodRoute methodRoute : router.routes().stream()
						.filter(route -> filterMethod(route, requestMethod))
						.collect(Collectors.toList())) {

					next.set(false);

					try {
						methodRoute.route().process(request, response);
					} catch (Exception e) {
						execute(exchange, e, request, response
								, methodRoute.exceptions()
								, router.exceptions()
								, alkun.exceptions());
						return;
					}

					if (!next.get()) {
						return;
					}

				}
			}
		}

//		if (exchange.getResponseCode() < 0) {
//			exchange.sendResponseHeaders(HttpStatus.OK_200.status(), 0);
//		}
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

	private boolean filterMethod(MethodRoute route, String requestedMethod) {
		return Filters.filterMethod(route.method().name(), requestedMethod);
	}
}
