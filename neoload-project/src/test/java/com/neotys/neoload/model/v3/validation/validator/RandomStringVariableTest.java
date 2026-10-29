package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.variable.RandomStringVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class RandomStringVariableTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_MIN_LENGTH;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'min_length': must be greater than or equal to 1 character.").append(LINE_SEPARATOR);
		CONSTRAINTS_MIN_LENGTH = sb.toString();
	}

	private static final String CONSTRAINTS_MAX_LENGTH;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 2.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': random_string 'min_length' must be less than or equal to 'max_length'.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'max_length': must be greater than or equal to 1 character.").append(LINE_SEPARATOR);
		CONSTRAINTS_MAX_LENGTH = sb.toString();
	}

	private static final String CONSTRAINTS_MIN_LENGTH_GREATER_THAN_MAX_LENGTH;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': random_string 'min_length' must be less than or equal to 'max_length'.").append(LINE_SEPARATOR);
		CONSTRAINTS_MIN_LENGTH_GREATER_THAN_MAX_LENGTH = sb.toString();
	}

	@Test
	public void validateMinLength() {
		final Validator validator = new Validator();

		final RandomStringVariable withNegativeMinLength = RandomStringVariable.builder()
				.name("MyRandomString")
				.minLength(0)
				.build();
		final Validation validation = validator.validate(withNegativeMinLength, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MIN_LENGTH, validation.getMessage().get());
	}

	@Test
	public void validateMaxLength() {
		final Validator validator = new Validator();

		final RandomStringVariable withNegativeMaxLength = RandomStringVariable.builder()
				.name("MyRandomString")
				.maxLength(-1)
				.build();
		final Validation validation = validator.validate(withNegativeMaxLength, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MAX_LENGTH, validation.getMessage().get());
	}

	@Test
	public void validateMinLengthNotGreaterThanMaxLength() {
		final Validator validator = new Validator();

		final RandomStringVariable withMinLengthGreaterThanMaxLength = RandomStringVariable.builder()
				.name("MyRandomString")
				.minLength(20)
				.maxLength(10)
				.build();
		Validation validation = validator.validate(withMinLengthGreaterThanMaxLength, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MIN_LENGTH_GREATER_THAN_MAX_LENGTH, validation.getMessage().get());

		final RandomStringVariable withMinLengthEqualToMaxLength = RandomStringVariable.builder()
				.name("MyRandomString")
				.minLength(10)
				.maxLength(10)
				.build();
		validation = validator.validate(withMinLengthEqualToMaxLength, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void validateDefaults() {
		final Validator validator = new Validator();

		final RandomStringVariable randomStringVariable = RandomStringVariable.builder()
				.name("MyRandomString")
				.build();
		final Validation validation = validator.validate(randomStringVariable, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
		assertEquals(5, randomStringVariable.getMinLength());
		assertEquals(10, randomStringVariable.getMaxLength());
		assertFalse(randomStringVariable.isPredictable());
	}
}
