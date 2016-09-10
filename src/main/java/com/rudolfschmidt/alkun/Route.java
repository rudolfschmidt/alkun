package com.rudolfschmidt.alkun;

@FunctionalInterface
public interface Route {
    /**
     * @param req  the express provided facade method for the server request object
     * @param res the express provided facade method for the server response object
     * @throws Exception
     */
    void process(Request req, Response res) throws Exception;
}
