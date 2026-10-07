package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class DurationAssertionTest {
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static final String CONSTRAINTS_DURATION_ASSERTION_LESS_THAN_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'less_than': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_DURATION_ASSERTION_LESS_THAN_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_DURATION_ASSERTION_LESS_THAN_NEGATIVE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'less_than': must be greater than or equal to 0.").append(LINE_SEPARATOR);
		CONSTRAINTS_DURATION_ASSERTION_LESS_THAN_NEGATIVE = sb.toString();
	}

	@Test
	public void validateLessThan() {
		final Validator validator = new Validator();

		DurationAssertion assertion = DurationAssertion.builder()
				.build();
		Validation validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DURATION_ASSERTION_LESS_THAN_NULL, validation.getMessage().get());

		assertion = DurationAssertion.builder()
				.lessThan(-1L)
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DURATION_ASSERTION_LESS_THAN_NEGATIVE, validation.getMessage().get());

		assertion = DurationAssertion.builder()
				.lessThan(2048L)
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
