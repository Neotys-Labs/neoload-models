package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface RegexpValidator extends Validator {
	String getValidationRegex();
}
