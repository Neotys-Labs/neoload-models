package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

// S2097 suppressed: the nested Jackson value-filter class overrides equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface OutOfValueVariable extends Variable {

	String OUT_OF_VALUE = "out_of_value";

	enum OutOfValue {
		@JsonProperty("cycle")
		CYCLE,
		@JsonProperty("stop_test")
		STOP,
		@JsonProperty("no_value_code")
		NO_VALUE;

		// NeoLoad legacy XML "whenOutOfValues" attribute code for this behaviour.
		public String getWhenOutOfValuesCode() {
			switch (this) {
				case CYCLE : return "CYCLE_VALUES";
				case STOP : return "STOP_TEST";
				case NO_VALUE : return "DEFAULT_VALUE";
				default : return "CYCLE_VALUES";
			}
		}
	}

	// Written only when it differs from its default value.
	@JsonProperty(OUT_OF_VALUE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOutOfValueFilter.class)
	@Value.Default
	default OutOfValue getOutOfValue() {
		return OutOfValue.CYCLE;
	}

	class DefaultOutOfValueFilter {
		@Override
		public boolean equals(final Object value) {
			return OutOfValue.CYCLE.equals(value);
		}

		@Override
		public int hashCode() {
			return OutOfValue.CYCLE.hashCode();
		}
	}
}
