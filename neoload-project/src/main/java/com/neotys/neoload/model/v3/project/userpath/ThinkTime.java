package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

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
