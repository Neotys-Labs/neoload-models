package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.*;
import com.neotys.neoload.model.v3.binding.serializer.MatchDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.List;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, WaitUntil.CONDITIONS, Match.MATCH, WaitUntil.TIMEOUT})
@JsonSerialize(as = ImmutableWaitUntil.class)
@JsonDeserialize(as = ImmutableWaitUntil.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface WaitUntil extends Step {

	String DEFAULT_NAME = "wait_until";
	String DEFAULT_TIMEOUT = "60000";
	String CONDITIONS = "conditions";
	String TIMEOUT = "timeout";

	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CONDITIONS)
	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	List<Condition> getConditions();

	@JsonProperty(Match.MATCH)
	@RequiredCheck(groups = {NeoLoad.class})
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultMatchFilter.class)
	@JsonSerialize(converter = MatchToStringConverter.class)
	@JsonDeserialize(using = MatchDeserializer.class)
	@Value.Default
	default Match getMatch() {
		return Match.ANY;
	}

	@JsonProperty(TIMEOUT)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTimeoutFilter.class)
	@JsonSerialize(converter= TimeDurationInMsOrInVariableToStringConverter.class)
	@JsonDeserialize(converter= StringToTimeDurationInMsOrInVariableConverter.class)
	@Value.Default
	default String getTimeout() {
		return DEFAULT_TIMEOUT;
	}

	// Jackson value filters excluding the default name / match from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_NAME.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	class DefaultTimeoutFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_TIMEOUT.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_TIMEOUT.hashCode();
		}
	}

	class DefaultMatchFilter {
		@Override
		public boolean equals(final Object value) {
			return Match.ANY.equals(value);
		}

		@Override
		public int hashCode() {
			return Match.ANY.hashCode();
		}
	}

	class Builder extends ImmutableWaitUntil.Builder {}
	static Builder builder() {
		return new Builder();
	}
}