package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class Router {

    private final String path;
    private final List<MethodRoute> routes = new ArrayList<>();
    private final ExceptionRoutes exceptions = new ExceptionRoutes();

    public Router(String path) {
        this.path = path;
    }

    public <T extends Exception> Router exception(Class<T> clazz, ExceptionRoute<T> route) {
        exceptions.addExceptionRoute(clazz, route);
        return this;
    }

    public MethodRoute use(Route route) {
        MethodRoute methodRoute = new MethodRoute(this, HttpMethod.ANY, route);
        routes.add(methodRoute);
        return methodRoute;
    }

    public MethodRoute get(Route route) {
        MethodRoute methodRoute = new MethodRoute(this, HttpMethod.GET, route);
        routes.add(methodRoute);
        return methodRoute;
    }

    public MethodRoute post(Route route) {
        MethodRoute methodRoute = new MethodRoute(this, HttpMethod.POST, route);
        routes.add(methodRoute);
        return methodRoute;
    }

    public MethodRoute put(Route route) {
        MethodRoute methodRoute = new MethodRoute(this, HttpMethod.PUT, route);
        routes.add(methodRoute);
        return methodRoute;
    }

    public MethodRoute delete(Route route) {
        MethodRoute methodRoute = new MethodRoute(this, HttpMethod.DELETE, route);
        routes.add(methodRoute);
        return methodRoute;
    }

    public MethodRoute head(Route route) {
        MethodRoute methodRoute = new MethodRoute(this, HttpMethod.HEAD, route);
        routes.add(methodRoute);
        return methodRoute;
    }

    public MethodRoute options(Route route) {
        MethodRoute methodRoute = new MethodRoute(this, HttpMethod.OPTIONS, route);
        routes.add(methodRoute);
        return methodRoute;
    }

    @Override
    public String toString() {
        return path;
    }

    protected String getPath() {
        return path;
    }

    protected List<MethodRoute> getRoutes() {
        return routes;
    }

    protected ExceptionRoutes getExceptions() {
        return exceptions;
    }
}
