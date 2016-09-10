package com.rudolfschmidt.alkun;

import java.util.Date;
import java.util.Optional;

public class CookieBuilder {

	private static final String SET_COOKIE = "Set-Cookie";
	private static final String VALUE_SEPARATOR = "=";
	private static final String ATTRIBUTE_SEPARATOR = "; ";
	private static final String DOMAIN = "domain";
	private static final String PATH = "path";
	private static final String SECURE_FLAG = "secure";
	private static final String HTTP_ONLY_FLAG = "httpOnly";

	private final Response response;
	private final String key;
	private final String value;

	private Date expiration;
	private String domain;
	private String path;
	private boolean secure;
	private boolean httpOnly;

	public CookieBuilder(Response response, String key, String value) {
		this.response = response;
		this.key = key;
		this.value = value;
	}

	public CookieBuilder expires(Date expiration) {
		this.expiration = expiration;
		return this;
	}

	public CookieBuilder domain(String domain) {
		this.domain = domain;
		return this;
	}

	public CookieBuilder path(String path) {
		this.path = path;
		return this;
	}

	public CookieBuilder secure() {
		this.secure = true;
		return this;
	}

	public CookieBuilder httpOnly() {
		this.httpOnly = true;
		return this;
	}

	public void send() {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append(VALUE_SEPARATOR);
		sb.append(value);
		if (Optional.ofNullable(domain).isPresent()) {
			sb.append(ATTRIBUTE_SEPARATOR);
			sb.append(DOMAIN);
			sb.append(VALUE_SEPARATOR);
			sb.append(domain);
		}
		if (Optional.ofNullable(path).isPresent()) {
			sb.append(ATTRIBUTE_SEPARATOR);
			sb.append(PATH);
			sb.append(VALUE_SEPARATOR);
			sb.append(path);
		}
		if (secure) {
			sb.append(ATTRIBUTE_SEPARATOR);
			sb.append(SECURE_FLAG);
		}
		if (httpOnly) {
			sb.append(ATTRIBUTE_SEPARATOR);
			sb.append(HTTP_ONLY_FLAG);
		}
		response.header(SET_COOKIE, sb.toString());
	}
}
