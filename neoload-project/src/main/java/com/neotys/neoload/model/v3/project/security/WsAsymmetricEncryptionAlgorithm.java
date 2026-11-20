package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WsAsymmetricEncryptionAlgorithm {
	RSA_V1_5("rsa_v1_5"),
	RSA_OAEP("rsa_oaep"),
	NONE("none");

	private final String value;

	WsAsymmetricEncryptionAlgorithm(final String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * Jackson value filter omitting the property from serialization when it equals the default ({@code rsa_v1_5}).
	 */
	public static class DefaultFilter {
		@Override
		public boolean equals(final Object value) { return RSA_V1_5.equals(value); }
		@Override
		public int hashCode() { return RSA_V1_5.hashCode(); }
	}
}
