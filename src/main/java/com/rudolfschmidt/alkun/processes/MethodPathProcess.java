package com.rudolfschmidt.alkun.processes;

import com.rudolfschmidt.alkun.http.HttpMethod;
import com.rudolfschmidt.alkun.handlers.RouteHandler;
import com.rudolfschmidt.alkun.routes.Route;

public class MethodPathProcess implements Process {

	public final HttpMethod method;
	public final String path;
	public final Route route;

	public MethodPathProcess(
			final HttpMethod method,
			final String path,
			final Route route
	) {
		this.method = method;
		this.path = path;
		this.route = route;
	}

	@Override
	public boolean handle(RouteHandler routeHandler) throws Exception {
		return routeHandler.handle(this);
	}

}
