package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableRandomUUIDVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, RandomUUIDVariable.UPPER_CASE, RandomUUIDVariable.PREDICTABLE,
		Variable.CHANGE_POLICY, Variable.SCOPE, Variable.ORDER, Variable.OUT_OF_VALUE})
// S2097 suppressed: DefaultChangePolicyFilter overrides equals(Object) to compare the property
// value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface RandomUUIDVariable extends Variable {

	String UPPER_CASE 			= "upper_case";
	String PREDICTABLE 			= "predictable";

	@JsonProperty(UPPER_CASE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RandomUUIDVariable.DefaultFalseFilter.class)
	@Value.Default
	default boolean isUpperCase() {
		return false;
	}

	@JsonProperty(PREDICTABLE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RandomUUIDVariable.DefaultFalseFilter.class)
	@Value.Default
	default boolean isPredictable() {
		return false;
	}

	// A random UUID is meant to change on every use, so it overrides the generic Variable
	// default of EACH_ITERATION.
	@Override
	@JsonProperty(CHANGE_POLICY)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RandomUUIDVariable.DefaultChangePolicyFilter.class)
	@Value.Default
	default ChangePolicy getChangePolicy() {
		return ChangePolicy.EACH_USE;
	}

	class DefaultChangePolicyFilter {
		@Override
		public boolean equals(final Object value) {
			return ChangePolicy.EACH_USE.equals(value);
		}

		@Override
		public int hashCode() {
			return ChangePolicy.EACH_USE.hashCode();
		}
	}

	class DefaultFalseFilter {
		@Override
		public boolean equals(final Object value) {
			return Boolean.FALSE.equals(value);
		}

		@Override
		public int hashCode() {
			return Boolean.FALSE.hashCode();
		}
	}

	class Builder extends ImmutableRandomUUIDVariable.Builder {}

	static Builder builder() {
		return new Builder();
	}
}
