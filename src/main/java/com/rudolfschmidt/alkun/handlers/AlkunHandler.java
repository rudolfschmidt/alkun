package com.rudolfschmidt.alkun.handlers;

import com.rudolfschmidt.alkun.processes.Process;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;

public class AlkunHandler implements HttpHandler {

	private final Deque<Process> processes;

	public AlkunHandler(
		final Deque<Process> processes
	) {
		this.processes = processes;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		try {
			process(httpExchange);
		} finally {
			httpExchange.close();
		}

	}

	private void process(HttpExchange httpExchange) throws IOException {

		final Iterator<Process> iterator = processes.iterator();

		while (iterator.hasNext()) {

			final Process process = iterator.next();
			final boolean next;

			try {
				next = process.handle(new DefaultRouteHandler(httpExchange));
			} catch (Exception exception) {
				processException(httpExchange, exception, iterator);
				return;
			}

			if (!next) {
				return;
			}

		}

	}

	private void processException(HttpExchange httpExchange, Exception exception, Iterator<Process> iterator) {

		while (iterator.hasNext()) {

			final Process process = iterator.next();
			final boolean next;

			try {
				next = process.handle(new ExceptionRouteHandler(httpExchange, exception));
			} catch (Exception ex) {
				processException(httpExchange, ex, iterator);
				return;
			}

			if (!next) {
				return;
			}

		}

	}
}
