package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.VariableModifierCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, VariableModifier.CATEGORY, VariableModifier.MODE,
		VariableModifier.VARIABLE_NAME, VariableModifier.VALUE})
@JsonDeserialize(as = ImmutableVariableModifier.class)
@JsonSerialize(as = ImmutableVariableModifier.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@VariableModifierCheck(groups = {NeoLoad.class})
public interface VariableModifier extends Step {

	//default values
	String DEFAULT_NAME = "variable_modifier";
	Category DEFAULT_CATEGORY = Category.PREDEFINED;
	Mode DEFAULT_MODE = Mode.NEXT_VALUE;
	//fields
	String CATEGORY = "category";
	String VARIABLE_NAME = "variable_name"; //handles GUI "Defined Variable - Name" + "Shared Queue - Name"
	String MODE = "mode";
	String VALUE = "value"; //handles GUI "Shared Queue - add value" + "Shared Queue - poll value"

	enum Category {
		@JsonProperty("predefined")
		PREDEFINED,
		@JsonProperty("shared_queue")
		SHARED_QUEUE
	}

	enum Mode {
		@JsonProperty("next_value")
		NEXT_VALUE,
		@JsonProperty("init_value")
		INIT_VALUE,
		@JsonProperty("add_shared_queue_value")
		ADD_SHARED_QUEUE_VALUE,
		@JsonProperty("poll_shared_queue")
		POLL_SHARED_QUEUE
	}

	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CATEGORY)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultCategoryFilter.class)
	@Value.Default
	default Category getCategory() {
		return DEFAULT_CATEGORY;
	}

	@JsonProperty(MODE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultModeFilter.class)
	@Value.Default
	default Mode getMode() {
		return DEFAULT_MODE;
	}

	@JsonProperty(VARIABLE_NAME)
	@RequiredCheck(groups = {NeoLoad.class})
	String getVariableName();

	@JsonProperty(VALUE)
	Optional<String> getValue(); //can be empty in case Category == PREDEFINED

	class Builder extends ImmutableVariableModifier.Builder {}
	static Builder builder() {
		return new Builder();
	}

	// Jackson value filters excluding the default name / match from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			if (value instanceof String) {
				return DEFAULT_NAME.equals(value);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	class DefaultCategoryFilter {
		@Override
		public boolean equals(final Object value) {
			if  (value instanceof Category) {
				return DEFAULT_CATEGORY.equals(value);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_CATEGORY.hashCode();
		}
	}

	class DefaultModeFilter {
		@Override
		public boolean equals(final Object value) {
			if  (value instanceof Mode) {
				return DEFAULT_MODE.equals(value);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_MODE.hashCode();
		}
	}
}