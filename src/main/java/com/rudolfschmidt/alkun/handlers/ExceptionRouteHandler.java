package com.rudolfschmidt.alkun.handlers;

import com.rudolfschmidt.alkun.processes.ExceptionProcess;
import com.rudolfschmidt.alkun.processes.FilterProcess;
import com.rudolfschmidt.alkun.processes.MethodPathProcess;
import com.rudolfschmidt.alkun.processes.PathProcess;
import com.rudolfschmidt.alkun.request.Request;
import com.rudolfschmidt.alkun.response.Response;
import com.sun.net.httpserver.HttpExchange;

class ExceptionRouteHandler implements RouteHandler {

	private final HttpExchange httpExchange;
	private final Exception exception;

	ExceptionRouteHandler(
			final HttpExchange httpExchange,
			final Exception exception
	) {
		this.httpExchange = httpExchange;
		this.exception = exception;
	}

	@Override
	public <T extends Exception> boolean handle(ExceptionProcess<T> process) throws Exception {

		if (!process.exceptionClass.isInstance(exception)) {
			return true;
		}

		final Request request = new Request(httpExchange, null);
		final Response response = new Response(httpExchange);
		final T castedException = process.exceptionClass.cast(exception);

		return process.exceptionRoute.process(castedException, request, response).isNext();

	}

	@Override
	public boolean handle(FilterProcess process) throws Exception {
		return true;
	}

	@Override
	public boolean handle(PathProcess process) throws Exception {
		return true;
	}

	@Override
	public boolean handle(MethodPathProcess process) throws Exception {
		return true;
	}

}
