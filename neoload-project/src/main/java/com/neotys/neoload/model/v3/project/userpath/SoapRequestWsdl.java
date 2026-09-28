package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({SoapRequestWsdl.PATH})
@JsonSerialize(as = ImmutableSoapRequestWsdl.class)
@JsonDeserialize(as = ImmutableSoapRequestWsdl.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface SoapRequestWsdl {
	String PATH = "path";

	@JsonProperty(PATH)
	String getPath();

	class Builder extends ImmutableSoapRequestWsdl.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
