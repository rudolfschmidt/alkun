package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.HttpMethod;

public class MethodRoute {

	private final Router router;
	private final HttpMethod method;
	private final Route route;
	private final ExceptionRoutes exceptions = new ExceptionRoutes();

	protected MethodRoute(Router router, HttpMethod method, Route route) {
		this.router = router;
		this.method = method;
		this.route = route;
	}

	public <T extends Exception> MethodRoute exception(Class<T> clazz, ExceptionRoute<T> route) {
		exceptions.addExceptionRoute(clazz, route);
		return this;
	}

	public MethodRoute use(Route route) {
		return router.use(route);
	}

	public MethodRoute get(Route route) {
		return router.use(route);
	}

	public MethodRoute post(Route route) {
		return router.use(route);
	}

	public MethodRoute put(Route route) {
		return router.use(route);
	}

	public MethodRoute delete(Route route) {
		return router.use(route);
	}

	public MethodRoute head(Route route) {
		return router.use(route);
	}

	public MethodRoute options(Route route) {
		return router.use(route);
	}

	public HttpMethod getMethod() {
		return method;
	}

	public Route getRoute() {
		return route;
	}

	public ExceptionRoutes getExceptions() {
		return exceptions;
	}
}
