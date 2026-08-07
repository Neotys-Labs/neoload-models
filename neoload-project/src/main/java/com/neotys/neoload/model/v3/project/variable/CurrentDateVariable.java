package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, DatePatternElement.PATTERN, DatePatternElement.INCREMENT_VALUE, DatePatternElement.INCREMENT_TIMEUNIT})
@JsonDeserialize(as = ImmutableCurrentDateVariable.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface CurrentDateVariable extends Variable, DatePatternElement {

	class Builder extends ImmutableCurrentDateVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}

}
