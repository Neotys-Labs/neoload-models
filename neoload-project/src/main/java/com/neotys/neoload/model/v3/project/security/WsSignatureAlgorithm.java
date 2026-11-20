package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WsSignatureAlgorithm {
	RSA_SHA1("rsa_sha1"),
	RSA_SHA256("rsa_sha256"),
	RSA_SHA384("rsa_sha384"),
	RSA_SHA512("rsa_sha512"),
	RSA_RIPEMD160("rsa_ripemd160"),
	RSA_MD5("rsa_md5"),
	HMAC_MD5("hmac_md5"),
	HMAC_RIPEMD160("hmac_ripemd160"),
	HMAC_SHA1("hmac_sha1"),
	HMAC_SHA256("hmac_sha256"),
	HMAC_SHA384("hmac_sha384"),
	HMAC_SHA512("hmac_sha512"),
	DSA_SHA1("dsa_sha1"),
	ECDSA_SHA1("ecdsa_sha1"),
	ECDSA_SHA256("ecdsa_sha256"),
	ECDSA_SHA384("ecdsa_sha384"),
	ECDSA_SHA512("ecdsa_sha512");

	private final String value;

	WsSignatureAlgorithm(final String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * Jackson value filter omitting the property from serialization when it equals the default ({@code rsa_sha1}).
	 */
	public static class DefaultFilter {
		@Override
		public boolean equals(final Object value) { return RSA_SHA1.equals(value); }
		@Override
		public int hashCode() { return RSA_SHA1.hashCode(); }
	}
}
