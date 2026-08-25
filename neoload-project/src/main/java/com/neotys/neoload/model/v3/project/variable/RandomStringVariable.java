package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableRandomStringVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, RandomStringVariable.MIN_LENGTH, RandomStringVariable.MAX_LENGTH,
		RandomStringVariable.PREDICTABLE, Variable.CHANGE_POLICY, Variable.SCOPE, Variable.ORDER, Variable.OUT_OF_VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface RandomStringVariable extends PolicyVariable {

	String MIN_LENGTH 			= "min_length";
	String MAX_LENGTH 			= "max_length";
	String PREDICTABLE 			= "predictable";

	@JsonProperty(MIN_LENGTH)
	@Value.Default
	@PositiveCheck(unit = "character", groups = {NeoLoad.class})
	default int getMinLength() {
		return 5;
	}

	@JsonProperty(MAX_LENGTH)
	@Value.Default
	@PositiveCheck(unit = "character", groups = {NeoLoad.class})
	default int getMaxLength() {
		return 10;
	}

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
