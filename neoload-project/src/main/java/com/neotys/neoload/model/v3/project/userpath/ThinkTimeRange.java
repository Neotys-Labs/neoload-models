package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({ThinkTimeRange.MIN, ThinkTimeRange.MAX})
@JsonSerialize(as = ImmutableThinkTimeRange.class)
@JsonDeserialize(as = ImmutableThinkTimeRange.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface ThinkTimeRange {
	String MIN = "min";
	String MAX = "max";

	@RequiredCheck(groups = {NeoLoad.class})
	@JsonProperty(MIN)
	String getMin();

	@RequiredCheck(groups = {NeoLoad.class})
	@JsonProperty(MAX)
	String getMax();

	class Builder extends ImmutableThinkTimeRange.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}