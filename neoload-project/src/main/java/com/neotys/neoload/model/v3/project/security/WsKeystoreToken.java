package com.neotys.neoload.model.v3.project.security;

/**
 * A WS-Security token using a key from a keystore declared in {@code web_services_security.keystores}.
 */
public interface WsKeystoreToken extends WsToken {
	String getKeystore();
}
