package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Delay.DELAY})
@JsonDeserialize(as = ImmutableDelay.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Delay extends StepDuration {
	String DELAY = "delay";

	class Builder extends ImmutableDelay.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
