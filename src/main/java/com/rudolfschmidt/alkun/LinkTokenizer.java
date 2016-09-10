package com.rudolfschmidt.alkun;

import java.util.ArrayList;
import java.util.List;

class LinkTokenizer {

	public static List<String> tokenize(String str, String delimiter) {
		return tokenize(str, delimiter.charAt(0));
	}

	public static List<String> tokenize(String str, char delimiter) {
		List<String> ret = new ArrayList<>();
		String buffer = "";
		for (char c : str.toCharArray()) {
			if (c == delimiter) {
				ret.add(buffer);
				buffer = "";
			} else {
				buffer += c;
			}
		}
		ret.add(buffer);
		return ret;
	}

}
