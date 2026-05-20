package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableCurrentDateVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, CurrentDateVariable.PATTERN, CurrentDateVariable.INC_TYPE, CurrentDateVariable.INC_VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface CurrentDateVariable extends Variable {

	String PATTERN = "pattern";
	String INC_TYPE = "inc_type";
	String INC_VALUE = "inc_value";

	@JsonProperty(PATTERN)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPattern();

	@JsonProperty(INC_TYPE)
	@Value.Default
	default DateVariable.IncType getIncType() {
		return DateVariable.IncType.SECOND;
	}

	@JsonProperty(INC_VALUE)
	@Value.Default
	default int getIncValue() {
		return 0;
	}

	class Builder extends ImmutableCurrentDateVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
