package com.rudolfschmidt.alkun.http;

import java.io.UnsupportedEncodingException;

public class URI {

	private final String initial;
	private Parameters parameters;

	public URI(String initial) {
		this.initial = initial;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public String build() throws UnsupportedEncodingException {
		return initial + (parameters != null && parameters.size() > 0 ? "?" + parameters.encode().format("%s=%s", "&") : "");
	}

}
