package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

// Subtypes are mapped to the generated Immutable* classes (not the interfaces) so that the
// polymorphic type id can be resolved at serialization time from the runtime object, which is
// always an Immutable* instance.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = ImmutableConstantVariable.class, name = "constant"),
		@JsonSubTypes.Type(value = ImmutableFileVariable.class, name = "file"),
		@JsonSubTypes.Type(value = ImmutableCounterVariable.class, name = "counter"),
		@JsonSubTypes.Type(value = ImmutableRandomNumberVariable.class, name = "random_number"),
		@JsonSubTypes.Type(value = ImmutableJavaScriptVariable.class, name = "javascript")

})
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface Variable extends Element {

	String ORDER 						= "order";
	String OUT_OF_VALUE 				= "out_of_value";

	enum Order {
		@JsonProperty("sequential")
		SEQUENTIAL,
		@JsonProperty("random")
		RANDOM,
		@JsonProperty("any")
		ANY
	}

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

	// Each of the two properties below is written only when it differs from its default value.
	@JsonProperty(ORDER)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOrderFilter.class)
	@Value.Default
	default Order getOrder() {
		return Order.ANY;
	}

	@JsonProperty(OUT_OF_VALUE)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOutOfValueFilter.class)
	@Value.Default
	default OutOfValue getOutOfValue() {
		return OutOfValue.CYCLE;
	}

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
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
