package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

public interface OutOfValueElement {

	String OUT_OF_VALUE = "out_of_value";

	OutOfValue DEFAULT_OUT_OF_VALUE = OutOfValue.CYCLE;

	enum OutOfValue {
		@JsonProperty("cycle")
		CYCLE,
		@JsonProperty("stop_test")
		STOP,
		@JsonProperty("no_value_code")
		NO_VALUE
	}

	@JsonProperty(OUT_OF_VALUE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOutOfValueFilter.class)
	@Value.Default
	default OutOfValue getOutOfValue() {
		return DEFAULT_OUT_OF_VALUE;
	}

	class DefaultOutOfValueFilter {
		@Override
		public boolean equals(final Object o) {
			if  (o instanceof OutOfValue) {
				return DEFAULT_OUT_OF_VALUE.equals(o);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_OUT_OF_VALUE.hashCode();
		}
	}
}
