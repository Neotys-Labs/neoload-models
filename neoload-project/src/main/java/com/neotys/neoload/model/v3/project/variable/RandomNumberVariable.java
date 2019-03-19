package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableRandomNumberVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, RandomNumberVariable.MIN, RandomNumberVariable.MAX, RandomNumberVariable.PREDICTABLE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface RandomNumberVariable extends Variable {

	String MIN = "min";
	String MAX = "max";
	String PREDICTABLE = "predictable";

	@JsonProperty(MIN)
	@RequiredCheck(groups = {NeoLoad.class})
	int getMin();

	@JsonProperty(MAX)
	@RequiredCheck(groups = {NeoLoad.class})
	int getMax();

	@JsonProperty(PREDICTABLE)
	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default boolean isPredictable() {
		return false;
	}


	class Builder extends ImmutableRandomNumberVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
