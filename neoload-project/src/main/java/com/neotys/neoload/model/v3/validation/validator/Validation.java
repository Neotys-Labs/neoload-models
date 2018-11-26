package com.neotys.neoload.model.v3.validation.validator;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Validation {
	boolean isValid();
	Optional<String> getMessage();
	
	class Builder extends ImmutableValidation.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
