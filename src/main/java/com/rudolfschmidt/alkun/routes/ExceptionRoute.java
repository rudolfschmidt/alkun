package com.rudolfschmidt.alkun.routes;

import com.rudolfschmidt.alkun.request.Request;
import com.rudolfschmidt.alkun.response.Response;
import com.rudolfschmidt.alkun.response.Result;

@FunctionalInterface
public interface ExceptionRoute<T extends Exception> {
	Result process(T ex, Request req, Response res) throws Exception;
}