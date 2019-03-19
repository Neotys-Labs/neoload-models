package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableCounterVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, CounterVariable.START, CounterVariable.END, CounterVariable.INCREMENT})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface CounterVariable extends Variable {

	String START = "start";
	String END = "end";
	String INCREMENT = "increment";

	@JsonProperty(START)
	@RequiredCheck(groups = {NeoLoad.class})
	int getStart();

	@JsonProperty(END)
	@RequiredCheck(groups = {NeoLoad.class})
	int getEnd();

	@JsonProperty(INCREMENT)
	@RequiredCheck(groups = {NeoLoad.class})
	int getIncrement();

	class Builder extends ImmutableCounterVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
