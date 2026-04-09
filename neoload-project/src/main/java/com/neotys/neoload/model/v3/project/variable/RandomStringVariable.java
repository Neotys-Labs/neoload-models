package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableRandomStringVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, RandomStringVariable.MIN_LENGTH, RandomStringVariable.MAX_LENGTH, RandomStringVariable.PREDICTABLE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface RandomStringVariable extends Variable {

	String MIN_LENGTH = "min_length";
	String MAX_LENGTH = "max_length";
	String PREDICTABLE = "predictable";

	@JsonProperty(MIN_LENGTH)
	@RequiredCheck(groups = {NeoLoad.class})
	int getMinLength();

	@JsonProperty(MAX_LENGTH)
	@RequiredCheck(groups = {NeoLoad.class})
	int getMaxLength();

	@JsonProperty(PREDICTABLE)
	@Value.Default
	default boolean isPredictable() {
		return false;
	}

	class Builder extends ImmutableRandomStringVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}
}