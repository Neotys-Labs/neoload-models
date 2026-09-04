package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.DatePatternCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface DatePatternVariable extends Variable {

	String PATTERN = "pattern";
	String INCREMENT_VALUE = "increment_value";
	String INCREMENT_TIMEUNIT = "increment_timeunit";

	String DEFAULT_PATTERN = "dd/MM/yyyy HH:mm:ss";

	enum IncrementTimeUnit {
		@JsonProperty("millisecond")
		MILLISECOND,
		@JsonProperty("second")
		SECOND,
		@JsonProperty("minute")
		MINUTE,
		@JsonProperty("hour")
		HOUR,
		@JsonProperty("day")
		DAY,
		@JsonProperty("month")
		MONTH,
		@JsonProperty("year")
		YEAR;

		// NeoLoad legacy "DateIncrementTypes" code for this increment time unit.
		public int getDateIncrementTypeCode() {
			switch (this) {
				case MILLISECOND : return -1;
				case SECOND : return 0;
				case MINUTE : return 1;
				case HOUR : return 2;
				case DAY : return 3;
				case MONTH : return 4;
				case YEAR : return 5;
				default : return 0;
			}
		}
	}

	// Written only when it differs from its default value.
	@JsonProperty(PATTERN)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultPatternFilter.class)
	@Value.Default
	@DatePatternCheck(groups = {NeoLoad.class})
	default String getPattern() {
		return DEFAULT_PATTERN;
	}

	// Jackson enforces integer-only deserialization (negative and positive) via the int primitive type.
	@JsonProperty(INCREMENT_VALUE)
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	@Value.Default
	default int getIncrementValue() {
		return 0;
	}

	// Written only when it differs from its default value.
	@JsonProperty(INCREMENT_TIMEUNIT)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultIncrementTimeUnitFilter.class)
	@Value.Default
	default IncrementTimeUnit getIncrementTimeUnit() {
		return IncrementTimeUnit.SECOND;
	}

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

	class DefaultIncrementTimeUnitFilter {
		@Override
		public boolean equals(final Object value) {
			return IncrementTimeUnit.SECOND.equals(value);
		}

		@Override
		public int hashCode() {
			return IncrementTimeUnit.SECOND.hashCode();
		}
	}
}
