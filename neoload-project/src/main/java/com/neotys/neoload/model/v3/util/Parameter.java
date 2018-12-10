package com.neotys.neoload.model.v3.util;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Parameter {
	String getName();
	
	// If value has empty optional, it means the parameter has no value ("parameterName"), 
	// If value is present with empty string, it means value is empty string ("parameterName=")
	Optional<String> getValue();
	
	class Builder extends ImmutableParameter.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
