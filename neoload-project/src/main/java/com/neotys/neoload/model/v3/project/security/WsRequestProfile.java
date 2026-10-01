package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsRequestProfile.NAME, WsRequestProfile.HEADERS})
@JsonSerialize(as = ImmutableWsRequestProfile.class)
@JsonDeserialize(as = ImmutableWsRequestProfile.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsRequestProfile {
	String NAME = "name";
	String HEADERS = "headers";

	@JsonProperty(NAME)
	@RequiredCheck(groups = {NeoLoad.class})
	String getName();

	@JsonProperty(HEADERS)
	@Valid
	List<WsSecurityHeader> getHeaders();

	class Builder extends ImmutableWsRequestProfile.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
