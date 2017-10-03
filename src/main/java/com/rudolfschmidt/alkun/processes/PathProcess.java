package com.rudolfschmidt.alkun.processes;

import com.rudolfschmidt.alkun.handlers.RouteHandler;
import com.rudolfschmidt.alkun.routes.Route;

public class PathProcess implements Process {

	public final String path;
	public final Route route;

	public PathProcess(
			final String path,
			final Route route
	) {
		this.path = path;
		this.route = route;
	}

	@Override
	public boolean handle(RouteHandler routeHandler) throws Exception {
		return routeHandler.handle(this);
	}

}
