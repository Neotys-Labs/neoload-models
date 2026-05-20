package com.neotys.neoload.model.v3.project.userpath.assertion;

import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({DurationAssertion.NAME, DurationAssertion.VALUE})
@JsonSerialize(as = ImmutableDurationAssertion.class)
@JsonDeserialize(as = ImmutableDurationAssertion.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface DurationAssertion extends Assertion {
	String NAME = "name";
	String VALUE = "value";

	@JsonProperty(NAME)
	Optional<String> getName();

	@JsonProperty(VALUE)
	@RequiredCheck(groups = {NeoLoad.class})
	@RangeCheck(min = 0, groups = {NeoLoad.class})
	long getValue();

	class Builder extends ImmutableDurationAssertion.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
