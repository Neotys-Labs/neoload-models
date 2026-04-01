package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.VariableModifierCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import java.util.Optional;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, VariableModifier.CATEGORY, VariableModifier.MODE,
		VariableModifier.VARIABLE_NAME, VariableModifier.SHARED_VARIABLE_NAME,
		VariableModifier.SRC_VARIABLE_NAME, VariableModifier.DEST_VARIABLE_NAME})
@JsonDeserialize(as = ImmutableVariableModifier.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@VariableModifierCheck(groups = {NeoLoad.class})
public interface VariableModifier extends Step {

	String DEFAULT_NAME = "variable_modifier";
	String CATEGORY = "category";
	String MODE = "mode";
	String VARIABLE_NAME = "variable_name";
	String SHARED_VARIABLE_NAME = "shared_variable_name";
	String SRC_VARIABLE_NAME = "src_variable_name";
	String DEST_VARIABLE_NAME = "dest_variable_name";

	enum Category {
		@JsonProperty("defined")
		DEFINED,
		@JsonProperty("shared")
		SHARED
	}

	enum Mode {
		@JsonProperty("next_value")
		NEXT_VALUE,
		@JsonProperty("init_value")
		INIT_VALUE,
		@JsonProperty("add_value")
		ADD_VALUE,
		@JsonProperty("get_value")
		GET_VALUE
	}

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CATEGORY)
	@Value.Default
	default Category getCategory() {
		return Category.DEFINED;
	}

	@JsonProperty(MODE)
	@RequiredCheck(groups = {NeoLoad.class})
	Mode getMode();

	@JsonProperty(VARIABLE_NAME)
	Optional<String> getVariableName();

	@JsonProperty(SHARED_VARIABLE_NAME)
	Optional<String> getSharedVariableName();

	@JsonProperty(SRC_VARIABLE_NAME)
	Optional<String> getSrcVariableName();

	@JsonProperty(DEST_VARIABLE_NAME)
	Optional<String> getDestVariableName();

	class Builder extends ImmutableVariableModifier.Builder {}
	static Builder builder() {
		return new Builder();
	}
}