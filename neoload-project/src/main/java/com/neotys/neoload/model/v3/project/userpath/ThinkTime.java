package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

import javax.validation.constraints.Pattern;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ThinkTime.THINK_TIME})
@JsonDeserialize(as = ImmutableThinkTime.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface ThinkTime extends ActionDuration {
	String THINK_TIME = "think_time";

	class Builder extends ImmutableThinkTime.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
