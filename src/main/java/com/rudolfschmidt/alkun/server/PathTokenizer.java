package com.rudolfschmidt.alkun.server;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathTokenizer {

	public static List<String> tokenizePath(String path) {
		return Stream.of(path.split(PathTokens.SLASH)).filter(PathTokenizer::notEmpty).collect(Collectors.toList());
	}

	private static boolean notEmpty(String str) {
		return !str.isEmpty();
	}

}
