package com.neotys.models.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface TextValidator extends Validator {
	String getValidationText();
}
