package com.rudolfschmidt.alkun.processes;

import com.rudolfschmidt.alkun.handlers.RouteHandler;
import com.rudolfschmidt.alkun.routes.ExceptionRoute;

public class ExceptionProcess<T extends Exception> implements Process {

	public final Class<T> exceptionClass;
	public final ExceptionRoute<T> exceptionRoute;

	public ExceptionProcess(
			final Class<T> exceptionClass,
			final ExceptionRoute<T> exceptionRoute
	) {
		this.exceptionClass = exceptionClass;
		this.exceptionRoute = exceptionRoute;
	}

	@Override
	public boolean handle(RouteHandler routeHandler) throws Exception {
		return routeHandler.handle(this);
	}
}
