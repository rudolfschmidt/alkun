package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Router {

	private final String[] paths;
	private final List<MethodRoute> routes = new ArrayList<>();
	private final ExceptionRoutes exceptions = new ExceptionRoutes();

	public Router(String... paths) {
		this.paths = paths;
	}

	public <T extends Exception> Router exception(Class<T> clazz, ExceptionRoute<T> route) {
		exceptions.addExceptionRoute(clazz, route);
		return this;
	}

	public MethodRoute use(Route route) {
		return add(HttpMethod.ANY, route);
	}

	public MethodRoute get(Route route) {
		return add(HttpMethod.GET, route);
	}

	public MethodRoute post(Route route) {
		return add(HttpMethod.POST, route);
	}

	public MethodRoute put(Route route) {
		return add(HttpMethod.PUT, route);
	}

	public MethodRoute delete(Route route) {
		return add(HttpMethod.DELETE, route);
	}

	public MethodRoute head(Route route) {
		return add(HttpMethod.HEAD, route);
	}

	public MethodRoute options(Route route) {
		return add(HttpMethod.OPTIONS, route);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(routes.stream().map(MethodRoute::method).collect(Collectors.toList()));
		sb.append(":");
		sb.append(Arrays.toString(paths));
		return sb.toString();
	}

	protected String[] paths() {
		return paths;
	}

	protected List<MethodRoute> routes() {
		return routes;
	}

	protected ExceptionRoutes exceptions() {
		return exceptions;
	}

	private MethodRoute add(HttpMethod httpMethod, Route route) {
		MethodRoute methodRoute = new MethodRoute(this, httpMethod, route);
		routes.add(methodRoute);
		return methodRoute;
	}
}
