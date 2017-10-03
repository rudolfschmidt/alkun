package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.handlers.AlkunHandler;
import com.rudolfschmidt.alkun.processes.Process;
import com.rudolfschmidt.alkun.routes.RouteModule;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Alkun {

	private static final Logger LOGGER = Logger.getLogger(Alkun.class.getName());

	private final Deque<Process> processes = new ArrayDeque<>();

	private Integer httpPort;
	private Integer maxThreads = 20;

	public void loglevel(Level level) {
		final Logger logger = Logger.getLogger("");
		logger.setLevel(level);
		for (Handler handler : logger.getHandlers()) {
			handler.setLevel(level);
		}
	}

	public void port(int httpPort) {
		this.httpPort = httpPort;
	}

	public void threadPool(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public void routes(RouteModule routeModule) {
		routeModule.init();
		processes.addAll(routeModule.getProcesses());
	}

	/**
	 * Start the Alkun Server
	 */
	public void start() {

		Objects.requireNonNull(httpPort, "no port number provided");

		final HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(httpPort), 0);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Server Start Error", e);
			return;
		}

		server.setExecutor(Executors.newFixedThreadPool(maxThreads));
		server.createContext("/", new AlkunHandler(processes));

		server.start();

		LOGGER.log(Level.INFO, "Start Alkun at " + httpPort);

	}
}
