package com.neotys.neoload.model.v3.project.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import java.util.Optional;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableBasicAuthentication.class)
@JsonPropertyOrder({LoginPasswordAuthentication.LOGIN, LoginPasswordAuthentication.PASSWORD, BasicAuthentication.REALM})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface BasicAuthentication extends LoginPasswordAuthentication {
	String REALM = "realm";

	@JsonProperty(REALM)
	Optional<String> getRealm();
}
