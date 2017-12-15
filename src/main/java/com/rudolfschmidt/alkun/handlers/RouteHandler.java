package com.rudolfschmidt.alkun.handlers;

import com.rudolfschmidt.alkun.processes.*;

public interface RouteHandler {

	boolean handle(FilterProcess process) throws Exception;

	boolean handle(PathProcess process) throws Exception;

	boolean handle(MethodPathProcess process) throws Exception;

	boolean handle(MethodProcess process) throws Exception;

	<T extends Exception> boolean handle(ExceptionProcess<T> process) throws Exception;

}
