package com.rudolfschmidt.alkun;

import java.util.LinkedHashMap;
import java.util.Map;

class ExceptionRoutes {

    private final Map<Class<?>, ExceptionRoute> routes = new LinkedHashMap<>();

    public <T extends Exception> void addExceptionRoute(Class<T> clazz, ExceptionRoute<T> route) {
        routes.put(clazz, route);
    }

    public <T extends Exception> boolean hasInstanceOf(T instance) {
        return routes.keySet().stream().anyMatch(clazz -> clazz.isInstance(instance));
    }

    public <T extends Exception> void process(T exception, Request request, Response response) throws Exception {
        for (Class<?> exceptionClass : routes.keySet()) {
            if (exceptionClass.isInstance(exception)) {
                routes.get(exceptionClass).process(exception, request, response);
                return;
            }
        }
    }
}