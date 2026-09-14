package com.neotys.neoload.model.v3.project.userpath.assertion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.constraints.PositiveOrZero;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonPropertyOrder({DurationAssertion.LESS_THAN})
@JsonSerialize(as = ImmutableDurationAssertion.class)
@JsonDeserialize(as = ImmutableDurationAssertion.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface DurationAssertion {
	String LESS_THAN = "less_than";

	@JsonProperty(LESS_THAN)
	@RequiredCheck(groups = {NeoLoad.class})
	@PositiveOrZero(groups = {NeoLoad.class})
	Long getLessThan();

	class Builder extends ImmutableDurationAssertion.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
