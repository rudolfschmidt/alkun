package com.rudolfschmidt.alkun.http;

import com.rudolfschmidt.alkun.http.encoder.Encoder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parameters {

	private final List<Parameter> parameters = new ArrayList<>();

	public void add(String key, String value) {

		final String k = Optional.ofNullable(key).filter(str -> !str.isEmpty()).orElseThrow(IllegalArgumentException::new);

		Optional.ofNullable(value).ifPresent(v -> parameters.add(new Parameter(k, v)));

	}

	public void add(Parameters other) {

		parameters.addAll(other.parameters);

	}

	public Parameters encode() {

		final Parameters parameters = new Parameters();
		this.parameters.forEach(parameter -> parameters.add(Encoder.encode(parameter.key), Encoder.encode(parameter.value)));
		return parameters;

	}

	public Parameters sortByKeys() {

		final Parameters parameters = new Parameters();

		parameters.parameters.addAll(this.parameters);
		parameters.parameters.sort(Comparator.comparing(param -> param.key));

		return parameters;

	}

	public <R> Stream<R> map(Function<Parameter, ? extends R> mapper) {

		return parameters.stream().map(mapper);

	}

	public String format(String format, String delimiter) {

		return parameters.stream().map(param -> String.format(format, param.key, param.value)).collect(Collectors.joining(delimiter));

	}

	int size() {

		return parameters.size();

	}
}
