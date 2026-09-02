package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Strings;
import org.junit.Test;

public class ElementNameValidatorTest {
	private static final char[] FORBIDDEN_CHARS = { '£', '€', '$', '"', '[', ']', '<', '>', '|', '*', '¤', '?', '§',
			'µ', '#', '`', '@', '^', '²', '°', '¨', '\\' };

	@Test
	public void isValid() {
		assertTrue(new ElementNameValidator().isValid(null, null));
		assertTrue(new ElementNameValidator().isValid("", null));
		assertTrue(new ElementNameValidator().isValid("population", null));
	}

	@Test
	public void isValid_accentedCharsAccepted() {
		assertTrue(new ElementNameValidator().isValid("population_éàçüö", null));
	}

	@Test
	public void isValid_forbiddenCharsRejected() {
		for (final char forbidden : FORBIDDEN_CHARS) {
			assertFalse("char '" + forbidden + "' should be rejected", new ElementNameValidator().isValid("name" + forbidden, null));
		}
	}

	@Test
	public void isValid_lengthBoundaries() {
		assertTrue(new ElementNameValidator().isValid(Strings.repeat("a", 100), null));
		assertFalse(new ElementNameValidator().isValid(Strings.repeat("a", 101), null));
	}
}
