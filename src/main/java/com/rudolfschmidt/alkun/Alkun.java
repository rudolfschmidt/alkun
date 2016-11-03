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
	private final BiFunction<String, Optional<Object>, String> renderer;

	public static Alkun newInstance() {
		return new Alkun(new ArrayList<>(), new ExceptionRoutes(), (template, model) -> template);
	}

	private Alkun(List<Router> routers,
				  ExceptionRoutes exceptions,
				  BiFunction<String, Optional<Object>, String> renderer) {

		this.routers = routers;
		this.exceptions = exceptions;
		this.renderer = renderer;
	}

	public Alkun loglevel(Level level) {
		Logger logger = Logger.getLogger("");
		logger.setLevel(level);
		Arrays.stream(logger.getHandlers()).forEach(handler -> handler.setLevel(level));
		return this;
	}

	public Alkun render(BiFunction<String, Optional<Object>, String> engine) {
		return new Alkun(routers, exceptions, engine);
	}

	public <T extends Exception> Alkun exception(Class<T> clazz, ExceptionRoute<T> route) {
		exceptions.addExceptionRoute(clazz, route);
		return this;
	}

	public Router route(String... paths) {
		Router route = new Router(paths);
		routers.add(route);
		return route;
	}

	public void listen(int httpPort) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(httpPort), 0);
			server.setExecutor(Executors.newFixedThreadPool(20));
			server.createContext("/", new ServerRouter(this));
			server.start();
			LOGGER.info("Server started at " + httpPort);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed Server Start", e);
		}
	}

	protected BiFunction<String, Optional<Object>, String> renderer() {
		return renderer;
	}

	protected List<Router> routers() {
		return routers;
	}

	protected ExceptionRoutes exceptions() {
		return exceptions;
	}
}
