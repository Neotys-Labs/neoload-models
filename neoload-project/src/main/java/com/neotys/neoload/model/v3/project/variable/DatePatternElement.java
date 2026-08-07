package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.DatePatternCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

public interface DatePatternElement {

	String PATTERN = "pattern";
	String INCREMENT_VALUE = "increment_value";
	String INCREMENT_TIMEUNIT = "increment_timeunit";
	//default value:
	String DEFAULT_PATTERN = "dd/MM/yyyy HH:mm:ss";
	IncrementTimeUnit DEFAULT_INCREMENT_TIMEUNIT = IncrementTimeUnit.SECOND;

	enum IncrementTimeUnit {
		@JsonProperty("second") SECOND,
		@JsonProperty("minute") MINUTE,
		@JsonProperty("hour") HOUR,
		@JsonProperty("day") DAY,
		@JsonProperty("month") MONTH,
		@JsonProperty("year") YEAR
	}

	@JsonProperty(PATTERN)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultPatternFilter.class)
	@Value.Default
	@DatePatternCheck(groups = {NeoLoad.class})
	default String getPattern() {
		return DEFAULT_PATTERN;
	}

	// Jackson enforces integer-only deserialization (negative and positive) via the int primitive type.
	@JsonProperty(INCREMENT_VALUE)
	@JsonInclude(JsonInclude.Include.NON_DEFAULT) //since 0 is default int value in java
	@Value.Default
	default int getIncrementValue() {
		return 0;
	}

	@JsonProperty(INCREMENT_TIMEUNIT)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultIncrementTimeUnitFilter.class)
	@Value.Default
	default IncrementTimeUnit getIncrementTimeUnit() {
		return DEFAULT_INCREMENT_TIMEUNIT;
	}

	final class DefaultPatternFilter {
		@Override
		public boolean equals(Object o) {
			if (o instanceof String) {
				return DEFAULT_PATTERN.equals(o);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_PATTERN.hashCode();
		}
	}

	final class DefaultIncrementTimeUnitFilter {
		@Override
		public boolean equals(Object o) {
			if (o instanceof IncrementTimeUnit) {
				return DEFAULT_INCREMENT_TIMEUNIT == o;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_INCREMENT_TIMEUNIT.hashCode();
		}
	}
}
