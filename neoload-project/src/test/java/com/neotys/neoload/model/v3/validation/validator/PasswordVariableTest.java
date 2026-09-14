package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.variable.PasswordVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class PasswordVariableTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_VALUE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'value': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE = sb.toString();
	}

	@Test
	public void validateMissingValue() {
		final Validator validator = new Validator();

		final PasswordVariable withoutValue = PasswordVariable.builder()
				.name("MyPassword")
				.build();
		final Validation validation = validator.validate(withoutValue, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());
	}

	@Test
	public void validateEmptyValue() {
		final Validator validator = new Validator();

		final PasswordVariable withEmptyValue = PasswordVariable.builder()
				.name("MyPassword")
				.value("")
				.build();
		final Validation validation = validator.validate(withEmptyValue, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());
	}

	@Test
	public void validateRequired() {
		final Validator validator = new Validator();

		final PasswordVariable passwordVariable = PasswordVariable.builder()
				.name("MyPassword")
				.value("s3cr3t")
				.build();
		final Validation validation = validator.validate(passwordVariable, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
