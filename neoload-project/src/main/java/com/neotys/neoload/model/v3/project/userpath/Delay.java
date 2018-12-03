package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Delay.DELAY})
@JsonDeserialize(as = ImmutableDelay.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Delay extends Element {
	String DEFAULT_NAME = "#delay#";
	String DELAY = "delay";

	@JsonProperty(NAME)
	@RequiredCheck(groups={NeoLoad.class})
	@Override
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(DELAY)
	@RequiredCheck(groups = {NeoLoad.class})
	String getDelay();

	class Builder extends ImmutableDelay.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
