package com.rudolfschmidt.alkun;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticRoute implements Route {

    private final String staticFolder;

    public StaticRoute(String staticFolder) {
        this.staticFolder = staticFolder;
    }

    @Override
    public void process(Request req, Response res) throws Exception {
        String normalizedPath = req.path().replaceFirst(req.basePath(), "");
        Path filePath = Paths.get(staticFolder, normalizedPath);
        if (Files.isRegularFile(filePath)) {
            res.sendFile(filePath);
        } else {
            res.next();
        }
    }
}
