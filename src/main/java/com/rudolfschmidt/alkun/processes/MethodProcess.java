package com.rudolfschmidt.alkun.processes;

import com.rudolfschmidt.alkun.handlers.RouteHandler;
import com.rudolfschmidt.alkun.http.HttpMethod;
import com.rudolfschmidt.alkun.routes.Route;

public class MethodProcess implements Process {

	public final HttpMethod method;
	public final Route route;

	public MethodProcess(
			final HttpMethod method,
			final Route route
	) {
		this.method = method;
		this.route = route;
	}

	@Override
	public boolean handle(RouteHandler routeHandler) throws Exception {
		return routeHandler.handle(this);
	}

}
