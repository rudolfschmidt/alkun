package com.rudolfschmidt.alkun.processes;

import com.rudolfschmidt.alkun.handlers.RouteHandler;
import com.rudolfschmidt.alkun.routes.Route;

public class FilterProcess implements Process {

	public final Route route;

	public FilterProcess(
			final Route route
	) {
		this.route = route;
	}

	@Override
	public boolean handle(RouteHandler routeHandler) throws Exception {
		return routeHandler.handle(this);
	}

}
