package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.MinMaxRange;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.math.BigDecimal;
import java.util.Optional;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableRandomNumberVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, RandomNumberVariable.MIN, RandomNumberVariable.MAX, RandomNumberVariable.PREDICTABLE,
	ChangePolicyVariable.CHANGE_POLICY})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@MinNotGreaterThanMaxCheck(element = "random_number", groups = {NeoLoad.class})
public interface RandomNumberVariable extends ChangePolicyVariable, MinMaxRange {

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

	@Override
	default Optional<BigDecimal> comparableMin() {
		return Optional.of(BigDecimal.valueOf(getMin()));
	}

	@Override
	default Optional<BigDecimal> comparableMax() {
		return Optional.of(BigDecimal.valueOf(getMax()));
	}


	class Builder extends ImmutableRandomNumberVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
