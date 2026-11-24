package com.neotys.neoload.model.v3.project.security;

import java.util.Optional;

/**
 * A WS-Security token using a key from a keystore.
 * <p>
 * {@link #getKeystore()} either names the {@code path} of a keystore declared in {@code web_services_security.keystores},
 * or is {@code ./wss-keystores/} followed by a file name holding variables ({@code ${...}}, possibly mixed with text
 * such as {@code ./wss-keystores/client_${env}.p12}), resolved for each Virtual User to a file of the {@code wss-keystores/} folder.
 * {@link #getPassword()} is the password of that variable keystore; it is only allowed when the keystore holds a variable.
 */
public interface WsKeystoreToken extends WsToken {
	String getKeystore();

	Optional<String> getPassword();
}
