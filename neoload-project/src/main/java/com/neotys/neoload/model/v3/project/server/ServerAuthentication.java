package com.neotys.neoload.model.v3.project.server;

import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * An item of the {@code authentications} list of a server: a row of the GUI "Authorization" table. NeoLoad answers
 * the challenge the server sends (Basic, Digest, NTLM or Negotiate) with these credentials.
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonSerialize(as = ImmutableServerAuthentication.class)
@JsonDeserialize(as = ImmutableServerAuthentication.class)
@JsonPropertyOrder({LoginPasswordAuthentication.LOGIN, LoginPasswordAuthentication.PASSWORD, ServerAuthentication.REALM, ServerAuthentication.PREFERRED_PROTOCOL})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface ServerAuthentication extends LoginPasswordAuthentication {
	String REALM = "realm";
	String PREFERRED_PROTOCOL = "preferred_protocol";

	/**
	 * The protocol to use when the server answers with a Negotiate challenge. Without it, NeoLoad picks the protocol
	 * itself (the GUI "Default").
	 */
	enum PreferredProtocol {
		@JsonProperty("kerberos")
		KERBEROS,
		@JsonProperty("ntlm")
		NTLM
	}

	/** The realm (Basic, Digest) or the domain (NTLM, Negotiate). Without it, the credentials answer any realm. */
	@JsonProperty(REALM)
	Optional<String> getRealm();

	@JsonProperty(PREFERRED_PROTOCOL)
	Optional<PreferredProtocol> getPreferredProtocol();

	class Builder extends ImmutableServerAuthentication.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
