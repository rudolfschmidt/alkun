package com.rudolfschmidt.alkun.handlers;

import com.rudolfschmidt.alkun.http.HttpMethod;
import com.rudolfschmidt.alkun.processes.*;
import com.rudolfschmidt.alkun.server.PathTokens;
import com.rudolfschmidt.alkun.request.Request;
import com.rudolfschmidt.alkun.response.Response;
import com.rudolfschmidt.alkun.server.PathTokenizer;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class DefaultRouteHandler implements RouteHandler {

	private final HttpExchange httpExchange;

	DefaultRouteHandler(
			final HttpExchange httpExchange
	) {
		this.httpExchange = httpExchange;
	}

	@Override
	public boolean handle(FilterProcess process) throws Exception {

		final Request request = new Request(httpExchange);
		final Response response = new Response(httpExchange);

		return process.route.process(request, response).isNext();

	}

	@Override
	public boolean handle(PathProcess process) throws Exception {

		if (!acceptedPath(httpExchange.getRequestURI().getPath(), process.path)) {
			return true;
		}

		final Request request = new Request(httpExchange, process.path);
		final Response response = new Response(httpExchange);

		return process.route.process(request, response).isNext();

	}

	@Override
	public boolean handle(MethodProcess process) throws Exception {

		if (!acceptMethod(httpExchange.getRequestMethod(), process.method)) {
			return true;
		}

		final Request request = new Request(httpExchange);
		final Response response = new Response(httpExchange);

		return process.route.process(request, response).isNext();

	}

	@Override
	public boolean handle(MethodPathProcess process) throws Exception {

		if (!acceptMethod(httpExchange.getRequestMethod(), process.method)) {
			return true;
		}

		if (!acceptedPath(httpExchange.getRequestURI().getPath(), process.path)) {
			return true;
		}

		final Request request = new Request(httpExchange, process.path);
		final Response response = new Response(httpExchange);

		return process.route.process(request, response).isNext();

	}

	@Override
	public <T extends Exception> boolean handle(ExceptionProcess<T> process) throws Exception {

		return true;

	}

	private static boolean acceptMethod(String requestMethod, HttpMethod routeMethod) {

		return requestMethod.equals(routeMethod.name());

	}

	private static boolean acceptedPath(String requestedPath, String providedPath) {

		if (requestedPath.equals(providedPath)) {
			return true;
		}

		if (providedPath.endsWith("*")) {
			return requestedPath.startsWith(providedPath.substring(0, providedPath.length() - 1));
		}

		final List<String> requested = PathTokenizer.tokenizePath(requestedPath);
		final List<String> provided = PathTokenizer.tokenizePath(providedPath);

		if (requested.size() != provided.size()) {
			return false;
		}

		for (int i = 0; (i < requested.size()) && (i < provided.size()); i++) {

			final String a = requested.get(i);
			final String b = provided.get(i);

			if (!b.startsWith(PathTokens.PARAM_DELIMITER) && !b.equals(a)) {
				return false;
			}

		}

		return true;

	}

}
