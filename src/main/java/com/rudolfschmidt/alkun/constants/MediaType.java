package com.rudolfschmidt.alkun.constants;

import java.util.Optional;
import java.util.stream.Stream;

public enum MediaType {

    APPLICATION_JSON("application/json", "json"),
    APPLICATION_JAVASCRIPT("application/javascript", "js"),
    TEXT_PLAIN("text/plain", "txt"),
    TEXT_HTML("text/html", "html", "htm"),
    TEXT_CSS("text/css", "css"),
    TEXT_CSV("text/csv", "csv"),
    IMAGE_SVG("image/svg+xml", "svg");

    private final String type;
    private final String[] suffixe;

    MediaType(String type, String... suffixe) {
        this.type = type;
        this.suffixe = suffixe;
    }

    public String getType() {
        return type;
    }

    public static Optional<MediaType> findBySuffix(String suffix) {
        return Stream.of(values()).filter(mediaType -> filterSuffix(mediaType, suffix)).findAny();
    }

    private static boolean filterSuffix(MediaType mediaType, String suffix) {
        return Stream.of(mediaType.suffixe).anyMatch(str -> str.equals(suffix));
    }
}
