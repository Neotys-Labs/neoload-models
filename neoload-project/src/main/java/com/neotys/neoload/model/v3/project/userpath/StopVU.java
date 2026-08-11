package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, StopVU.START_NEW_VU})
@JsonDeserialize(as = ImmutableStopVU.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface StopVU extends Step {

	String DEFAULT_NAME = "stop_vu";
	String START_NEW_VU = "start_new_vu";

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(START_NEW_VU)
	@Value.Default
	default boolean getStartNewVU() {
		return true;
	}

	class Builder extends ImmutableStopVU.Builder {}
	static Builder builder() {
		return new Builder();
	}
}