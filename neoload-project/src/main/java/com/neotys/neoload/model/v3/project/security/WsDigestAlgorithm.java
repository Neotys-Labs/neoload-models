package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WsDigestAlgorithm {
	SHA1("sha1"),
	SHA256("sha256"),
	SHA384("sha384"),
	SHA512("sha512"),
	RIPEMD160("ripemd160");

	private final String value;

	WsDigestAlgorithm(final String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * Jackson value filter omitting the property from serialization when it equals the default ({@code sha1}).
	 */
	public static class DefaultFilter {
		@Override
		public boolean equals(final Object value) { return SHA1.equals(value); }
		@Override
		public int hashCode() { return SHA1.hashCode(); }
	}
}
