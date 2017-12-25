package com.rudolfschmidt.alkun.transformers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

import static com.rudolfschmidt.http.encoder.Encoder.decode;

public class FormTransformer {

	public static <T> Optional<T> fromForm(byte[] body, Class<T> model) {
		return fromForm(new String(body), model);
	}

	public static <T> Optional<T> fromForm(String body, Class<T> model) {

		return newInstance(model.getDeclaredConstructors())

				.map(instance -> {

					for (String str : body.split("&")) {

						final String[] pairs = str.split("=");

						if (pairs.length < 2) {
							continue;
						}

						for (Field field : model.getDeclaredFields()) {

							if (pairs[0].equals(field.getName())) {

								final String decoded = decode(pairs[1]);

								try {
									if (field.getType().equals(Number.class)) {
										field.set(instance, (Number) Double.valueOf(decoded));
									} else if (field.getType().equals(Boolean.class)) {
										field.set(instance, Boolean.valueOf(decoded));
									} else {
										field.set(instance, decoded);
									}
								} catch (IllegalAccessException e) {
									throw new FormEncodingException();
								}

							}

						}

					}

					return (T) instance;

				});
	}

	private static Optional<Object> newInstance(Constructor<?>[] declaredConstructors) {

		return Stream.of(declaredConstructors).findFirst().map(constructor -> {
			try {
				return constructor.newInstance(new Object[constructor.getParameterCount()]);
			} catch (ReflectiveOperationException e) {
				throw new FormEncodingException();
			}
		});

	}

}
