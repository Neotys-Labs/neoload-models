package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutablePasswordVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, PasswordVariable.VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface PasswordVariable extends Variable {

	String VALUE = "value";

	@JsonProperty(VALUE)
	@RequiredCheck(groups = {NeoLoad.class})
	String getValue();

	class Builder extends ImmutablePasswordVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}
}