package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidSchemaVersionValidatorTest {

	private final ValidSchemaVersionValidator validator = new ValidSchemaVersionValidator();

	@Test
	public void null_value_is_valid() {
		// Use @NotNull separately if a non-null value is required.
		assertTrue(validator.isValid(null, null));
	}

	@Test
	public void supported_version_is_valid() {
		assertTrue(validator.isValid("3.0", null));
	}

	@Test
	public void unsupported_version_is_invalid() {
		assertFalse(validator.isValid("3.999", null));
		assertFalse(validator.isValid("4.0", null));
		assertFalse(validator.isValid("bogus", null));
	}
}
