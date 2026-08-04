package com.neotys.neoload.model.v3.project.variable;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableListVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, ListVariable.COLUMN_NAMES, ListVariable.VALUES,
	Variable.CHANGE_POLICY, Variable.SCOPE, Variable.ORDER, Variable.OUT_OF_VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface ListVariable extends Variable {

	String COLUMN_NAMES 	= "column_names";
	String VALUES 			= "values";

	@JsonProperty(COLUMN_NAMES)
	@RequiredCheck(groups = {NeoLoad.class})
	List<String> getColumnNames();

	@JsonProperty(VALUES)
	@RequiredCheck(groups = {NeoLoad.class})
	List<List<String>> getValues();

	class Builder extends ImmutableListVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
