package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = ConstantVariable.class, name = "constant"),
		@JsonSubTypes.Type(value = FileVariable.class, name = "file"),
		@JsonSubTypes.Type(value = CounterVariable.class, name = "counter"),
		@JsonSubTypes.Type(value = RandomNumberVariable.class, name = "random_number"),
		@JsonSubTypes.Type(value = JavaScriptVariable.class, name = "javascript")

})
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

	@JsonProperty(CHANGE_POLICY)
	@Value.Default
	default ChangePolicy getChangePolicy() {
		return ChangePolicy.EACH_ITERATION;
	}

	@JsonProperty(SCOPE)
	@Value.Default
	default Scope getScope() {
		return Scope.GLOBAL;
	}

	@JsonProperty(ORDER)
	@Value.Default
	default Order getOrder() {
		return Order.ANY;
	}

	@JsonProperty(OUT_OF_VALUE)
	@Value.Default
	default OutOfValue getOutOfValue() {
		return OutOfValue.CYCLE;
	}
}
