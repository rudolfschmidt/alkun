package com.rudolfschmidt.alkun.misc;

import com.rudolfschmidt.alkun.constants.HttpHeader;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MultiPartEncoder {

    public static boolean isMultiPart(Headers headers) {
        return headers.getFirst(HttpHeader.CONTENT_TYPE).startsWith("multipart/form-data");
    }

    public static void encode(HttpExchange exchange, List<Part> parts) throws IOException {
        String boundary = exchange.getRequestHeaders().getFirst(HttpHeader.CONTENT_TYPE);
        boundary = boundary.substring(boundary.lastIndexOf("; ") + 2);
        boundary = boundary.substring(boundary.lastIndexOf("=") + 1);

        List<String> lines;
        try (
                InputStream stream = exchange.getRequestBody();
                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                BufferedReader buffer = new BufferedReader(reader)
        ) {
            lines = buffer.lines().collect(Collectors.toList());
        }
        iterate(lines.iterator(), parts, boundary);
    }

    private static void iterate(Iterator<String> iterator, List<Part> parts, String boundary) {
        if (!iterator.hasNext()) {
            return;
        }
        Optional<Part> part = Optional.empty();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.equals("--" + boundary)) {
                part = Optional.of(Part.newInstance());
            }
            break;
        }
        String regex = "^Content-Disposition: form-data; name=\"(.*?)\"; filename=\"(.*?)\"$";
        Matcher matcher = Pattern.compile(regex).matcher(iterator.next());
        if (matcher.matches()) {
            String formName = matcher.group(1);
            String fileName = matcher.group(2);
            part.ifPresent(p -> p.setFormName(formName));
            part.ifPresent(p -> p.setFileName(fileName));
        }
        regex = "^Content-Type: (.*)$";
        matcher = Pattern.compile(regex).matcher(iterator.next());
        if (!matcher.matches()) {
            return;
        }
        String type = matcher.group(1);
        if (!type.startsWith("text")) {
            return;
        }
        part.ifPresent(p -> p.setType(type));
        iterator.next();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.equals("--" + boundary + "--")) {
                part.ifPresent(parts::add);
            } else {
                part.ifPresent(p -> p.getBody().add(line));
            }
        }
        iterate(iterator, parts, boundary);
    }
}
