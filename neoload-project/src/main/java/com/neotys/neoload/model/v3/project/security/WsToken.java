package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsToken.USERNAME_TOKEN, WsToken.TIMESTAMP})
@JsonSerialize(as = ImmutableWsToken.class)
@JsonDeserialize(as = ImmutableWsToken.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsToken {
	String USERNAME_TOKEN = "username_token";
	String TIMESTAMP = "timestamp";

	@JsonProperty(USERNAME_TOKEN)
	@Valid
	Optional<WsUsernameToken> getUsernameToken();

	@JsonProperty(TIMESTAMP)
	@Valid
	Optional<WsTimestampToken> getTimestamp();

	class Builder extends ImmutableWsToken.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
