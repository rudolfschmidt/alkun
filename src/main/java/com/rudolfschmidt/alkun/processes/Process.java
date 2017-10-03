package com.rudolfschmidt.alkun.processes;

import com.rudolfschmidt.alkun.handlers.RouteHandler;

public interface Process {
	boolean handle(RouteHandler routeHandler) throws Exception;
}
