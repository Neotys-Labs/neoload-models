package com.neotys.neoload.model.repository;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
@Deprecated
public interface Parameter {
	String getName();
	
	// If value has empty optional, it means the parameter has no value ("parameterName"), 
	// If value is present with empty string, it means value is empty string ("parameterName=")
	Optional<String> getValue();
	
}
