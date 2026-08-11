package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

// S2097 suppressed: the nested Jackson value-filter class overrides equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface OrderVariable extends Variable {

	String ORDER = "order";

	enum Order {
		@JsonProperty("sequential")
		SEQUENTIAL,
		@JsonProperty("random")
		RANDOM,
		@JsonProperty("any")
		ANY
	}

	// Written only when it differs from its default value.
	@JsonProperty(ORDER)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOrderFilter.class)
	@Value.Default
	default Order getOrder() {
		return Order.ANY;
	}

	class DefaultOrderFilter {
		@Override
		public boolean equals(final Object value) {
			return Order.ANY.equals(value);
		}

		@Override
		public int hashCode() {
			return Order.ANY.hashCode();
		}
	}
}
