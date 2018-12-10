package com.neotys.neoload.model.repository;

import java.util.Optional;

@Deprecated
public interface Variable {
	
	enum VariablePolicy{
		EACH_USE, //1
		EACH_REQUEST, //2
		EACH_PAGE, // 3
		EACH_VUSER, //4
		EACH_ITERATION //5
	}
	
	enum VariableScope{//range
		UNIQUE, //4
		GLOBAL, //1
		LOCAL // 2
	}
	
	enum VariableNoValuesLeftBehavior{
		CYCLE, //CYCLE_VALUES
		STOP, // STOP_TEST
		NO_VALUE // DEFAULT_VALUE
	}
	
	enum VariableOrder{ //order
		SEQUENTIAL, //1
		RANDOM, //2
	}

	String getName();
	Optional<String> getDescription();
	Optional<VariableOrder> getOrder();
	VariablePolicy getPolicy();
	VariableScope getScope();
	Optional<VariableNoValuesLeftBehavior> getNoValuesLeftBehavior();
}
