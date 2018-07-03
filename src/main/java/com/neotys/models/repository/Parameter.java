package com.neotys.models.repository;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Parameter {
	String getName();
	//If there is not a value, that means that parameter has not a value
	//Overwise, that means there is a value (an "=" is present in the URL),
	//if this value is void getValue return a empty "string" 
	Optional<String> getValue();
}
