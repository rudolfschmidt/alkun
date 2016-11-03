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
		return router.post(route);
	}

	public MethodRoute put(Route route) {
		return router.put(route);
	}

	public MethodRoute delete(Route route) {
		return router.delete(route);
	}

	public MethodRoute head(Route route) {
		return router.head(route);
	}

	public MethodRoute options(Route route) {
		return router.options(route);
	}

	public HttpMethod method() {
		return method;
	}

	public Route route() {
		return route;
	}

	public ExceptionRoutes exceptions() {
		return exceptions;
	}
}
