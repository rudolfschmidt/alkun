package com.rudolfschmidt.alkun.routes;

import com.rudolfschmidt.alkun.http.HttpMethod;
import com.rudolfschmidt.alkun.processes.*;
import com.rudolfschmidt.alkun.processes.Process;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class RouteModule {

	private final Deque<Process> processes;

	protected RouteModule() {
		this.processes = new ArrayDeque<>();
	}

	public abstract void init();

	public Deque<Process> getProcesses() {
		return processes;
	}

	protected void filter(Route route) {
		processes.add(new FilterProcess(route));
	}

	protected void filter(String routePath, Route route) {
		processes.add(new PathProcess(routePath, route));
	}

	protected void get(String routePath, Route route) {
		processes.add(new MethodPathProcess(HttpMethod.GET, routePath, route));
	}

	protected void post(String path, Route route) {
		processes.add(new MethodPathProcess(HttpMethod.POST, path, route));
	}

	protected void put(String path, Route route) {
		processes.add(new MethodPathProcess(HttpMethod.PUT, path, route));
	}

	protected void delete(String path, Route route) {
		processes.add(new MethodPathProcess(HttpMethod.DELETE, path, route));
	}

	protected void head(String path, Route route) {
		processes.add(new MethodPathProcess(HttpMethod.HEAD, path, route));
	}

	protected void options(String path, Route route) {
		processes.add(new MethodPathProcess(HttpMethod.OPTIONS, path, route));
	}

	protected <T extends Exception> void exception(Class<T> exceptionClass, ExceptionRoute<T> exceptionRoute) {
		processes.add(new ExceptionProcess<>(exceptionClass, exceptionRoute));
	}

}
