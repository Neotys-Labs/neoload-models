package com.neotys.neoload.model.validation.validator;

import java.util.Optional;

import org.immutables.value.Value;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@Deprecated
public interface Validation {
	boolean isValid();
	Optional<String> getMessage();
	
	class Builder extends ImmutableValidation.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
