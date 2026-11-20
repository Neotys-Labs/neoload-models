package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WsSymmetricEncryptionAlgorithm {
	TRIPLE_DES("triple_des"),
	AES_128("aes_128"),
	AES_192("aes_192"),
	AES_256("aes_256");

	private final String value;

	WsSymmetricEncryptionAlgorithm(final String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * Jackson value filter omitting the property from serialization when it equals the default ({@code aes_128}).
	 */
	public static class DefaultFilter {
		@Override
		public boolean equals(final Object value) { return AES_128.equals(value); }
		@Override
		public int hashCode() { return AES_128.hashCode(); }
	}
}
