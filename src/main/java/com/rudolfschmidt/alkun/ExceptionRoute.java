package com.rudolfschmidt.alkun;

@FunctionalInterface
public interface ExceptionRoute<T> {
    void process(T ex, Request req, Response res) throws Exception;
}
