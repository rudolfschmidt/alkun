package com.rudolfschmidt.alkun.handlers;

import com.rudolfschmidt.alkun.processes.ExceptionProcess;
import com.rudolfschmidt.alkun.processes.FilterProcess;
import com.rudolfschmidt.alkun.processes.MethodPathProcess;
import com.rudolfschmidt.alkun.processes.PathProcess;

public interface RouteHandler {

	boolean handle(FilterProcess process) throws Exception;

	boolean handle(PathProcess process) throws Exception;

	boolean handle(MethodPathProcess process) throws Exception;

	<T extends Exception> boolean handle(ExceptionProcess<T> process) throws Exception;

}
