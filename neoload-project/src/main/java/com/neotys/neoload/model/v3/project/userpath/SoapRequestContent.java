package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({SoapRequestContent.PATH})
@JsonSerialize(as = ImmutableSoapRequestContent.class)
@JsonDeserialize(as = ImmutableSoapRequestContent.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface SoapRequestContent {
	String PATH = "path";

	@JsonProperty(PATH)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPath();

	class Builder extends ImmutableSoapRequestContent.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
