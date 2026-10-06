package com.neotys.neoload.model.v3.project.security;

public enum WsSimplePasswordType implements WsPasswordType {
	PLAIN_TEXT("plain_text"),
	DIGEST("digest");

	private final String value;

	WsSimplePasswordType(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
