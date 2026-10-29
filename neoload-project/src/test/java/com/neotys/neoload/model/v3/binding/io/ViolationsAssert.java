package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import java.io.IOException;
import org.junit.function.ThrowingRunnable;

/**
 * Asserts the whole message of an invalid {@link Validation}: the violation count and each violation, in order.
 * Also asserts the errors raised while reading a duration.
 */
final class ViolationsAssert {
	static final String MIN_GREATER_THAN_MAX = "duration 'min' must be less than or equal to 'max'.";
	static final String MISSING_VALUE = "missing value or value is empty.";
	static final String VALUE_COMBINED_WITH_BOUNDS = "Incorrect duration: 'value' cannot be combined with 'min' or 'max'.";
	static final String NAME_NOT_ALLOWED = "Incorrect duration: 'name' and 'description' are not allowed.";

	private static final String LINE_SEPARATOR = System.lineSeparator();

	private ViolationsAssert() {
	}

	static String invalidValue(final String path, final String message) {
		return "Incorrect value for '" + path + "': " + message;
	}

	/**
	 * Asserts a read error: its message and the field the Jackson reference chain ends on.
	 */
	static void assertReadError(final IOException exception, final String message, final String field) {
		final String actual = exception.getMessage();
		assertTrue(actual, actual.startsWith(message));
		assertTrue(actual, actual.endsWith("$Json[\"" + field + "\"])"));
	}

	/**
	 * Asserts that a read fails on a raw duration text, the Jackson reference chain ending on the field.
	 */
	static void assertInvalidDurationRead(final ThrowingRunnable read, final String rawValue, final String field) {
		final String actual = assertThrows(JsonMappingException.class, read).getMessage();
		assertTrue(actual, actual.startsWith("Incorrect duration: '" + rawValue + "' is not a valid duration."));
		assertTrue(actual, actual.contains("[\"" + field + "\"]"));
	}

	static void assertViolations(final Validation validation, final String... violations) {
		assertFalse(validation.isValid());
		final StringBuilder expected = new StringBuilder("Data Model is invalid. Violation Number: " + violations.length + ".").append(LINE_SEPARATOR);
		for (int i = 0; i < violations.length; i++) {
			expected.append("Violation ").append(i + 1).append(" - ").append(violations[i]).append(LINE_SEPARATOR);
		}
		assertEquals(expected.toString(), validation.getMessage().get());
	}
}
