package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableCurrentDateVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, DatePatternVariable.PATTERN, DatePatternVariable.INCREMENT_VALUE, DatePatternVariable.INCREMENT_TIMEUNIT})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface CurrentDateVariable extends Variable, DatePatternVariable {

	class Builder extends ImmutableCurrentDateVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
