package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Request.URL})
@JsonDeserialize(as = ImmutableRequest.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Request extends Action {
	String URL = "url";
	String DEFAULT_NAME = "#request#";

	@JsonIgnore
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(URL)
	@RequiredCheck(groups={NeoLoad.class})
	String getUrl();
	
	class Builder extends ImmutableRequest.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
