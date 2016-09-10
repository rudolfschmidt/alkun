package com.rudolfschmidt.alkun;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Alkun {

	private static final Logger LOGGER = Logger.getLogger(Alkun.class.getName());

	private final List<Router> routers;
	private final ExceptionRoutes exceptions;
	private BiFunction<String, Optional<Object>, String> engine;

	public Alkun() {
		this.routers = new ArrayList<>();
		this.exceptions = new ExceptionRoutes();
	}

	public void loglevel(Level level) {
		Logger logger = Logger.getLogger("");
		logger.setLevel(level);
		Arrays.stream(logger.getHandlers()).forEach(handler -> handler.setLevel(level));
	}

	public BiFunction<String, Optional<Object>, String> engine() {
		return this.engine;
	}

	public void engine(BiFunction<String, Optional<Object>, String> engine) {
		this.engine = engine;
	}

	public <T extends Exception> void exception(Class<T> clazz, ExceptionRoute<T> route) {
		exceptions.addExceptionRoute(clazz, route);
	}

	public Router route(String path) {
		Router route = new Router(path);
		routers.add(route);
		return route;
	}

	public void listen(int httpPort) {
		if (!Optional.ofNullable(engine).isPresent()) {
			LOGGER.log(Level.INFO, "No Render Engine set");
		}
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(httpPort), 0);
			server.setExecutor(Executors.newFixedThreadPool(20));
			server.createContext("/", new ServerRouter(this, routers, exceptions));
			server.start();
			LOGGER.info("Server started at " + httpPort);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed Server Start", e);
		}
	}
}
