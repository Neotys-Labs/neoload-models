package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WsPartEncoding {
	ELEMENT("element"),
	CONTENT("content");

	private final String value;

	WsPartEncoding(final String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * Jackson value filter omitting the property from serialization when it equals the default ({@code element}).
	 */
	public static class DefaultFilter {
		@Override
		public boolean equals(final Object value) { return ELEMENT.equals(value); }
		@Override
		public int hashCode() { return ELEMENT.hashCode(); }
	}
}
