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
@JsonSerialize(as = ImmutableBasicAuthentication.class)
@JsonDeserialize(as = ImmutableBasicAuthentication.class)
@JsonPropertyOrder({LoginPasswordAuthentication.LOGIN, LoginPasswordAuthentication.PASSWORD, BasicAuthentication.REALM})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface BasicAuthentication extends LoginPasswordAuthentication {
	String REALM = "realm";

	@JsonProperty(REALM)
	Optional<String> getRealm();

	class Builder extends ImmutableBasicAuthentication.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
