package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neotys.neoload.model.v3.project.Element;

// Subtypes are mapped to the generated Immutable* classes (not the interfaces) so that the
// polymorphic type id can be resolved at serialization time from the runtime object, which is
// always an Immutable* instance.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = ImmutableConstantVariable.class, name = "constant"),
		@JsonSubTypes.Type(value = ImmutablePasswordVariable.class, name = "password"),
		@JsonSubTypes.Type(value = ImmutableFileVariable.class, name = "file"),
		@JsonSubTypes.Type(value = ImmutableCounterVariable.class, name = "counter"),
		@JsonSubTypes.Type(value = ImmutableRandomNumberVariable.class, name = "random_number"),
		@JsonSubTypes.Type(value = ImmutableRandomStringVariable.class, name = "random_string"),
		@JsonSubTypes.Type(value = ImmutableRandomUUIDVariable.class, name = "random_uuid"),
		@JsonSubTypes.Type(value = ImmutableJavaScriptVariable.class, name = "javascript"),
		@JsonSubTypes.Type(value = ImmutableSharedQueueVariable.class, name = "shared_queue"),
		@JsonSubTypes.Type(value = ImmutableListVariable.class, name = "list"),
})
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface Variable extends Element {

	String CHANGE_POLICY 				= "change_policy";
	String SCOPE 						= "scope";
	String ORDER 						= "order";
	String OUT_OF_VALUE 				= "out_of_value";

	enum ChangePolicy {
		@JsonProperty("each_use")
		EACH_USE,
		@JsonProperty("each_request")
		EACH_REQUEST,
		@JsonProperty("each_page")
		EACH_PAGE,
		@JsonProperty("each_user")
		EACH_USER,
		@JsonProperty("each_iteration")
		EACH_ITERATION
	}

	enum Scope {
		@JsonProperty("unique")
		UNIQUE,
		@JsonProperty("global")
		GLOBAL,
		@JsonProperty("local")
		LOCAL
	}

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
		NO_VALUE
	}

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultChangePolicyFilter {
		@Override
		public boolean equals(final Object value) {
			return ChangePolicy.EACH_ITERATION.equals(value);
		}

		@Override
		public int hashCode() {
			return ChangePolicy.EACH_ITERATION.hashCode();
		}
	}

	class DefaultScopeFilter {
		@Override
		public boolean equals(final Object value) {
			return Scope.GLOBAL.equals(value);
		}

		@Override
		public int hashCode() {
			return Scope.GLOBAL.hashCode();
		}
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
