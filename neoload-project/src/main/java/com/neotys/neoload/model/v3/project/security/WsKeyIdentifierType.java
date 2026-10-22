package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WsKeyIdentifierType {
	DIRECT_REFERENCE("direct_reference"),
	DIRECT_REFERENCE_FIRST_CERTIFICATE("direct_reference_first_certificate"),
	ISSUER_REFERENCE("issuer_reference"),
	X509_KEY_IDENTIFIER("x509_key_identifier"),
	SUBJECT_KEY_IDENTIFIER("subject_key_identifier"),
	THUMBPRINT_SHA1("thumbprint_sha1"),
	ENCRYPTED_KEY_SHA1("encrypted_key_sha1");

	private final String value;

	WsKeyIdentifierType(final String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * Jackson value filter omitting the property from serialization when it equals the default ({@code issuer_reference}).
	 */
	public static class DefaultFilter {
		@Override
		public boolean equals(final Object value) { return ISSUER_REFERENCE.equals(value); }
		@Override
		public int hashCode() { return ISSUER_REFERENCE.hashCode(); }
	}
}
