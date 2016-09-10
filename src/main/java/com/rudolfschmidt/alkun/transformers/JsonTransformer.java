package com.rudolfschmidt.alkun.transformers;


import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class JsonTransformer {

	public static String render(Object model) {
		StringBuilder sb = new StringBuilder();
		if (model instanceof Map) {
			renderMap((Map) model, sb);
		} else if (model instanceof Object[]) {
			renderList(Arrays.asList((Object[]) model), sb);
		} else if (model instanceof Iterable) {
			renderList((Iterable) model, sb);
		} else {
			renderObject(model, sb);
		}
		return sb.toString();
	}

	private static void renderMap(Map map, StringBuilder sb) {
		boolean first = true;
		sb.append("{");
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			if (!first) {
				sb.append(",");
			}
			renderKeyValue(key.toString(), value, sb);
			first = false;
		}
		sb.append("}");
	}

	private static void renderList(Iterable iterable, StringBuilder sb) {
		sb.append("[");
		boolean first = true;
		for (Object element : iterable) {
			if (!first) {
				sb.append(",");
			}
			if (element.getClass() == String.class) {
				sb.append(element);
			} else {
				renderObject(element, sb);
			}
			first = false;
		}
		sb.append("]");
	}

	private static void renderObject(Object model, StringBuilder sb) {

		List<Field> filtered = Arrays.stream(model.getClass().getDeclaredFields())
				.filter(field -> !field.isAnnotationPresent(JsonIgnore.class))
				.peek(field -> field.setAccessible(true))
				.filter(field -> {
							Object value;
							try {
								value = field.get(model);
							} catch (IllegalAccessException e) {
								throw new RuntimeException(e);
							}
							return Optional.ofNullable(value).isPresent()
									|| (value instanceof Number
									&& !Double.isNaN(Double.parseDouble(value.toString())));
						}
				).collect(Collectors.toList());

		boolean first = true;
		sb.append("{");
		for (Field field : filtered) {
			Object value;
			try {
				value = field.get(model);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (!first) {
				sb.append(",");
			}
			boolean rendered = renderKeyValue(field.getName(), value, sb);
			if (!rendered) {
				sb.append(render(value));
			}
			first = false;
		}
		sb.append("}");
	}

	private static boolean renderKeyValue(String key, Object value, StringBuilder sb) {
		sb.append("\"").append(key).append("\"").append(":");
		if (value.getClass() == String.class) {
			renderString(value, sb);
			return true;
		} else if (value.getClass() == Boolean.class) {
			renderBoolean(value, sb);
			return true;
		} else if (value.getClass() == Date.class) {
			renderDate(value, sb);
			return true;
		} else if (value.getClass() == LocalTime.class) {
			renderPlainString(value.toString(), sb);
			return true;
		} else if (value instanceof Number) {
			sb.append(value);
			return true;
		}
		return false;
	}


	private static StringBuilder renderDate(Object value, StringBuilder sb) {
		return sb.append(((Date) value).getTime());
	}

	private static void renderBoolean(Object value, StringBuilder sb) {
		sb.append(Boolean.valueOf(value.toString()));
	}

	private static void renderString(Object value, StringBuilder sb) {
		String str = value.toString();
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\"", "\\\\\"");
		str = str.replaceAll("\\t", "\\s");
		str = str.replaceAll("\\n", "\\\\n");
		str = str.replaceAll("\\r", "\\\\r");
		renderPlainString(str, sb);
	}

	private static void renderPlainString(String str, StringBuilder sb) {
		sb.append("\"").append(str).append("\"");
	}
}
