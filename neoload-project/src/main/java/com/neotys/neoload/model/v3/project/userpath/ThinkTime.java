package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ThinkTime.THINK_TIME})
@JsonDeserialize(as = ImmutableThinkTime.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface ThinkTime extends Action {
	String DEFAULT_NAME = "#thinktime#";
	String THINK_TIME = "think_time";

	@JsonProperty(NAME)
	@RequiredCheck(groups={NeoLoad.class})
	@Override
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(THINK_TIME)
	@RequiredCheck(groups = {NeoLoad.class})
	String getThinkTime();

	class Builder extends ImmutableThinkTime.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
