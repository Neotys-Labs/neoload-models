package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;


public class SizeAssertionTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String CONSTRAINTS_SIZE_ASSERTION_INVALID;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': invalid attributes usage (at least one of equals, greater_than or less_than is required, and equals cannot be combined with greater_than or less_than).").append(LINE_SEPARATOR);
		CONSTRAINTS_SIZE_ASSERTION_INVALID = sb.toString();
	}

	@Test
	public void validateAtLeastOneBoundIsRequired() {
		final Validator validator = new Validator();

		final SizeAssertion assertion = SizeAssertion.builder().build();
		final Validation validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SIZE_ASSERTION_INVALID, validation.getMessage().get());
	}

	@Test
	public void validateEqualsIsExclusiveWithBounds() {
		final Validator validator = new Validator();

		SizeAssertion assertion = SizeAssertion.builder()
				.equals(1024L)
				.greaterThan(1L)
				.build();
		Validation validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SIZE_ASSERTION_INVALID, validation.getMessage().get());

		assertion = SizeAssertion.builder()
				.equals(1024L)
				.lessThan(2048L)
				.build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SIZE_ASSERTION_INVALID, validation.getMessage().get());
	}

	@Test
	public void validateAcceptedShapes() {
		final Validator validator = new Validator();

		SizeAssertion assertion = SizeAssertion.builder().equals(1024L).build();
		Validation validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		assertion = SizeAssertion.builder().greaterThan(1024L).build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		assertion = SizeAssertion.builder().lessThan(2048L).build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		assertion = SizeAssertion.builder().greaterThan(1024L).lessThan(2048L).build();
		validation = validator.validate(assertion, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}

	@Test
	public void validateNegativeSizeIsRejected() {
		final Validator validator = new Validator();

		final SizeAssertion assertion = SizeAssertion.builder().equals(-1L).build();
		final Validation validation = validator.validate(assertion, NeoLoad.class);
		assertFalse(validation.isValid());
	}
}
