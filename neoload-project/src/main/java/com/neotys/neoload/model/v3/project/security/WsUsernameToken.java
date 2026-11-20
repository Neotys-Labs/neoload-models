package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsUsernameToken.USERNAME, WsUsernameToken.PASSWORD, WsUsernameToken.PASSWORD_TYPE, WsUsernameToken.NONCE, WsUsernameToken.CREATED})
@JsonDeserialize(as = ImmutableWsUsernameToken.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsUsernameToken extends WsToken {
	String USERNAME = "username";
	String PASSWORD = "password";
	String PASSWORD_TYPE = "password_type";
	String NONCE = "nonce";
	String CREATED = "created";

	@JsonProperty(USERNAME)
	@RequiredCheck(groups = {NeoLoad.class})
	String getUsername();

	@JsonProperty(PASSWORD)
	Optional<String> getPassword();

	@JsonProperty(PASSWORD_TYPE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultPasswordTypeFilter.class)
	@Valid
	@Value.Default
	default WsPasswordType getPasswordType() {
		return WsSimplePasswordType.DIGEST;
	}

	@JsonProperty(NONCE)
	@JsonInclude(Include.NON_DEFAULT)
	@Value.Default
	default boolean getNonce() {
		return false;
	}

	@JsonProperty(CREATED)
	@JsonInclude(Include.NON_DEFAULT)
	@Value.Default
	default boolean getCreated() {
		return false;
	}

	/**
	 * Resets {@code nonce} and {@code created} to their default with {@code digest}: WSS4J always adds both
	 * for this password type, so the values are meaningless and must not be written back.
	 */
	@Value.Check
	default WsUsernameToken normalize() {
		if (WsSimplePasswordType.DIGEST.equals(getPasswordType()) && (getNonce() || getCreated())) {
			return builder().from(this).nonce(false).created(false).build();
		}
		return this;
	}

	/**
	 * Jackson value filter omitting {@code password_type} from serialization when it equals the default ({@code digest}).
	 */
	class DefaultPasswordTypeFilter {
		@Override
		public boolean equals(final Object value) { return WsSimplePasswordType.DIGEST.equals(value); }
		@Override
		public int hashCode() { return WsSimplePasswordType.DIGEST.hashCode(); }
	}

	class Builder extends ImmutableWsUsernameToken.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
