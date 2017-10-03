package com.rudolfschmidt.alkun.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Http {

	static {
		System.setProperty("http.keepAlive", "false");
	}

	public static HttpURLConnection get(URI uri) throws IOException {
		final URL url = new URL(uri.build());
		final URLConnection connection = url.openConnection();
		final HttpURLConnection httpConnection = (HttpURLConnection) connection;
		httpConnection.setRequestMethod("GET");
		httpConnection.setRequestProperty("Accept", "*/*");
		return httpConnection;
	}

	public static HttpURLConnection post(URI uri) throws IOException {
		final URL url = new URL(uri.build());
		final URLConnection connection = url.openConnection();
		final HttpURLConnection httpConnection = (HttpURLConnection) connection;
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Accept", "*/*");
		return httpConnection;
	}

}
