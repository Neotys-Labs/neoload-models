package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.ChangeStepCheck;
import com.neotys.neoload.model.v3.validation.constraints.DatePatternCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.StartDateMatchesPatternCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;

// S2097 suppressed: the nested Jackson value-filter class overrides equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableDateVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, DateVariable.PATTERN, DateVariable.START_DATE, DateVariable.CHANGE_STEP,
	ChangePolicyVariable.CHANGE_POLICY, ScopeVariable.SCOPE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@StartDateMatchesPatternCheck(groups = {NeoLoad.class})
public interface DateVariable extends ChangePolicyVariable, ScopeVariable {

	String PATTERN = "pattern";
	String START_DATE = "start_date";
	String CHANGE_STEP = "change_step";

	String DEFAULT_PATTERN = "dd/MM/yyyy HH:mm:ss";

	@JsonProperty(START_DATE)
	@RequiredCheck(groups = {NeoLoad.class})
	String getStartDate();

	// Written only when it differs from its default value.
	@JsonProperty(PATTERN)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultPatternFilter.class)
	@Value.Default
	@DatePatternCheck(groups = {NeoLoad.class})
	default String getPattern() {
		return DEFAULT_PATTERN;
	}

	@JsonProperty(CHANGE_STEP)
	@ChangeStepCheck(groups = {NeoLoad.class})
	Optional<String> getChangeStep();

	class DefaultPatternFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_PATTERN.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_PATTERN.hashCode();
		}
	}

	class Builder extends ImmutableDateVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
