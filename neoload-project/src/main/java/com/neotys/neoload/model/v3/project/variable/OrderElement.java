package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

public interface OrderElement {

	String ORDER = "order";

	Order DEFAULT_ORDER = Order.ANY;

	enum Order {
		@JsonProperty("sequential")
		SEQUENTIAL,
		@JsonProperty("random")
		RANDOM,
		@JsonProperty("any")
		ANY
	}

	@JsonProperty(ORDER)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOrderFilter.class)
	@Value.Default
	default Order getOrder() {
		return DEFAULT_ORDER;
	}

	class DefaultOrderFilter {
		@Override
		public boolean equals(final Object o) {
			if  (o instanceof Order) {
				return DEFAULT_ORDER.equals(o);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return DEFAULT_ORDER.hashCode();
		}
	}
}
