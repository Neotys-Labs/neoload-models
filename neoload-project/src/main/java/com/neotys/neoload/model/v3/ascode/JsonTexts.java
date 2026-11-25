package com.neotys.neoload.model.v3.ascode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;

/** Writes JSON with the indentation of the surrounding as-code schema. */
final class JsonTexts {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private JsonTexts() {
	}

	static String newline(final String text) {
		return text.contains("\r\n") ? "\r\n" : "\n";
	}

	static String lineIndent(final String text, final int index) {
		final int start = text.lastIndexOf('\n', index - 1) + 1;
		int end = start;
		while (end < text.length() && (text.charAt(end) == '\t' || text.charAt(end) == ' ')) {
			end++;
		}
		return text.substring(start, end);
	}

	static String write(final JsonNode node, final String unit, final String indent) {
		try {
			if (node.isValueNode()) {
				return MAPPER.writeValueAsString(node);
			}
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
		if (node.isArray()) {
			return writeArray(node, unit, indent);
		}
		return writeObject(node, unit, indent);
	}

	private static String writeObject(final JsonNode node, final String unit, final String indent) {
		if (node.isEmpty()) {
			return "{}";
		}
		final StringBuilder out = new StringBuilder();
		out.append('{').append('\n');
		final Iterator<String> names = node.fieldNames();
		while (names.hasNext()) {
			final String name = names.next();
			out.append(indent).append(unit).append(writeScalar(name)).append(": ");
			out.append(write(node.get(name), unit, indent + unit));
			if (names.hasNext()) {
				out.append(',');
			}
			out.append('\n');
		}
		out.append(indent).append('}');
		return out.toString();
	}

	private static String writeArray(final JsonNode node, final String unit, final String indent) {
		if (node.isEmpty()) {
			return "[]";
		}
		final StringBuilder out = new StringBuilder();
		out.append('[').append('\n');
		for (int index = 0; index < node.size(); index++) {
			out.append(indent).append(unit).append(write(node.get(index), unit, indent + unit));
			if (index + 1 < node.size()) {
				out.append(',');
			}
			out.append('\n');
		}
		out.append(indent).append(']');
		return out.toString();
	}

	private static String writeScalar(final String text) {
		try {
			return MAPPER.writeValueAsString(text);
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	static int matchingBrace(final String text, final int open) {
		char opener = text.charAt(open);
		char closer = opener == '{' ? '}' : ']';
		int depth = 0;
		boolean inString = false;
		boolean escaped = false;
		for (int index = open; index < text.length(); index++) {
			final char current = text.charAt(index);
			if (inString) {
				if (escaped) {
					escaped = false;
				} else if (current == '\\') {
					escaped = true;
				} else if (current == '"') {
					inString = false;
				}
				continue;
			}
			if (current == '"') {
				inString = true;
			} else if (current == opener) {
				depth++;
			} else if (current == closer && --depth == 0) {
				return index;
			}
		}
		throw new IllegalArgumentException("Unclosed JSON at " + open);
	}
}
