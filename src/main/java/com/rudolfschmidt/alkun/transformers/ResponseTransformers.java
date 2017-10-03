package com.rudolfschmidt.alkun.transformers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResponseTransformers {

	public static void toStream(InputStream in, OutputStream out) throws IOException {

		final byte[] buffer = new byte[1024];

		for (int b = in.read(buffer); b != -1; b = in.read(buffer)) {
			out.write(buffer, 0, b);
		}

	}

	public static byte[] toBytes(InputStream inputStream) throws IOException {

		try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			toStream(inputStream, outputStream);

			return outputStream.toByteArray();

		}

	}

}
