package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsSecurityHeader.ACTOR, WsSecurityHeader.MUST_UNDERSTAND, WsSecurityHeader.TOKENS})
@JsonSerialize(as = ImmutableWsSecurityHeader.class)
@JsonDeserialize(as = ImmutableWsSecurityHeader.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsSecurityHeader {
	String ACTOR = "actor";
	String MUST_UNDERSTAND = "must_understand";
	String TOKENS = "tokens";

	@JsonProperty(ACTOR)
	Optional<String> getActor();

	@JsonProperty(MUST_UNDERSTAND)
	@JsonInclude(Include.NON_DEFAULT)
	@Value.Default
	default boolean getMustUnderstand() {
		return false;
	}

	@JsonProperty(TOKENS)
	@Valid
	List<WsToken> getTokens();

	class Builder extends ImmutableWsSecurityHeader.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
