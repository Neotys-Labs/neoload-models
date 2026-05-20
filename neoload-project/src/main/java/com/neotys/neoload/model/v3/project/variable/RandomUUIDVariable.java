package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableRandomUUIDVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, RandomUUIDVariable.UPPER_CASE, RandomUUIDVariable.PREDICTABLE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface RandomUUIDVariable extends Variable {

	String UPPER_CASE = "upper_case";
	String PREDICTABLE = "predictable";

	@JsonProperty(UPPER_CASE)
	@Value.Default
	default boolean isUpperCase() {
		return false;
	}

	@JsonProperty(PREDICTABLE)
	@Value.Default
	default boolean isPredictable() {
		return false;
	}

	class Builder extends ImmutableRandomUUIDVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
