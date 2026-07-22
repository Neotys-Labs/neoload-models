package com.neotys.neoload.model.v3.project.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import java.util.Optional;

@JsonInclude(value=Include.NON_EMPTY)
@JsonSerialize(as = ImmutableNtlmAuthentication.class)
@JsonDeserialize(as = ImmutableNtlmAuthentication.class)
@JsonPropertyOrder({LoginPasswordAuthentication.LOGIN, LoginPasswordAuthentication.PASSWORD, NtlmAuthentication.DOMAIN})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface NtlmAuthentication extends LoginPasswordAuthentication {

	String DOMAIN = "domain";

	@JsonProperty(DOMAIN)
	Optional<String> getDomain();

	class Builder extends ImmutableNtlmAuthentication.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
