package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.StartDateMatchesPatternCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, DatePatternElement.PATTERN, DateVariable.START_DATE,
		DatePatternElement.INCREMENT_VALUE, DatePatternElement.INCREMENT_TIMEUNIT, ChangePolicyElement.CHANGE_POLICY,
		ScopeElement.SCOPE})
@JsonDeserialize(as = ImmutableDateVariable.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@StartDateMatchesPatternCheck(groups = {NeoLoad.class})
public interface DateVariable extends Variable, DatePatternElement, ChangePolicyElement, ScopeElement {

	String START_DATE = "start_date";

	//default values:
	ChangePolicy DEFAULT_CHANGE_POLICY = ChangePolicy.EACH_USE; //the default is not EACH_ITERATION

	@JsonProperty(START_DATE)
	@RequiredCheck(groups = {NeoLoad.class})
	String getStartDate();

	@JsonProperty(CHANGE_POLICY)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultChangePolicyFilter.class)
	@Value.Default
	@Override
	default ChangePolicy getChangePolicy() {
		return DEFAULT_CHANGE_POLICY;
	}

	class Builder extends ImmutableDateVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultChangePolicyFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_CHANGE_POLICY.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_CHANGE_POLICY.hashCode();
		}
	}

}
