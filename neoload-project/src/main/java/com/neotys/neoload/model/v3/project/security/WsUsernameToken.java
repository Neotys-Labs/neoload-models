package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsUsernameToken.USERNAME, WsUsernameToken.PASSWORD, WsUsernameToken.PASSWORD_TYPE, WsUsernameToken.NONCE, WsUsernameToken.CREATED, WsUsernameToken.SALT, WsUsernameToken.ITERATION})
@JsonSerialize(as = ImmutableWsUsernameToken.class)
@JsonDeserialize(as = ImmutableWsUsernameToken.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsUsernameToken {
	String USERNAME = "username";
	String PASSWORD = "password";
	String PASSWORD_TYPE = "password_type";
	String NONCE = "nonce";
	String CREATED = "created";
	String SALT = "salt";
	String ITERATION = "iteration";

	enum PasswordType {
		@JsonProperty("text")
		TEXT,
		@JsonProperty("digest")
		DIGEST,
		@JsonProperty("derived")
		DERIVED
	}

	@JsonProperty(USERNAME)
	@RequiredCheck(groups = {NeoLoad.class})
	String getUsername();

	@JsonProperty(PASSWORD)
	Optional<String> getPassword();

	@JsonProperty(PASSWORD_TYPE)
	Optional<PasswordType> getPasswordType();

	@JsonProperty(NONCE)
	Optional<Boolean> getNonce();

	@JsonProperty(CREATED)
	Optional<Boolean> getCreated();

	@JsonProperty(SALT)
	Optional<String> getSalt();

	@JsonProperty(ITERATION)
	Optional<Integer> getIteration();

	class Builder extends ImmutableWsUsernameToken.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
