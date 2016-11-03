package com.rudolfschmidt.alkun;

import com.rudolfschmidt.alkun.constants.HttpMethod;
import com.rudolfschmidt.alkun.constants.RequestConstants;

import java.util.List;

public class Filters {

	static boolean filterPath(String providedPath, String requestedPath) {

		if (providedPath.equals(requestedPath)) {
			return true;
		}

		if (providedPath.endsWith("/*")) {
			return requestedPath.startsWith(providedPath.substring(0, providedPath.length() - 1));
		}

		if (providedPath.replaceAll("[^/]", "").length() != requestedPath.replaceAll("[^/]", "").length()) {
			return false;
		}

		List<String> providedTokens = LinkTokenizer.tokenize(providedPath, RequestConstants.PATH_TOKEN);
		List<String> requestedTokens = LinkTokenizer.tokenize(requestedPath, RequestConstants.PATH_TOKEN);

		for (int i = 0; i < providedTokens.size(); i++) {
			String providedToken = providedTokens.get(i);
			String requestedToken = requestedTokens.get(i);
			if (!providedToken.startsWith(RequestConstants.PATH_PARAM_DELIMITER)
					&& !providedToken.equals(requestedToken)) {
				return false;
			}
		}

		return true;
	}

	static boolean filterMethod(String providedMethod, String requestedMethod) {
		return providedMethod.equals(requestedMethod) || HttpMethod.valueOf(providedMethod) == HttpMethod.ANY;
	}
}
