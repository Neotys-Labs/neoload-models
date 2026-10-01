package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WebServicesSecurity.KEYSTORES, WebServicesSecurity.REQUEST_PROFILES, WebServicesSecurity.RESPONSE_PROFILES})
@JsonSerialize(as = ImmutableWebServicesSecurity.class)
@JsonDeserialize(as = ImmutableWebServicesSecurity.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WebServicesSecurity {
	String KEYSTORES = "keystores";
	String REQUEST_PROFILES = "request_profiles";
	String RESPONSE_PROFILES = "response_profiles";

	@JsonProperty(KEYSTORES)
	@Valid
	List<WsKeystore> getKeystores();

	@JsonProperty(REQUEST_PROFILES)
	@Valid
	List<WsRequestProfile> getRequestProfiles();

	@JsonProperty(RESPONSE_PROFILES)
	@Valid
	List<WsResponseProfile> getResponseProfiles();

	class Builder extends ImmutableWebServicesSecurity.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
