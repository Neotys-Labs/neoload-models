package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DigitsValidatorTest {
	@Test
	public void isValid() {
		assertTrue(new DigitsValidator().isValid(null, null));
		assertFalse(new DigitsValidator().isValid(0, null));
		
		assertFalse(new DigitsValidator().isValid("", null));
		assertFalse(new DigitsValidator().isValid("0", null));
		
		DigitsValidator validator = new DigitsValidator();
		validator.maxIntegerLength = 1;
		validator.maxFractionLength = 0;
		assertTrue(validator.isValid(0, null));
		assertTrue(validator.isValid("0", null));
		assertTrue(validator.isValid(9, null));
		assertTrue(validator.isValid("9", null));
		assertFalse(validator.isValid(10, null));
		assertFalse(validator.isValid("10", null));

		validator = new DigitsValidator();
		validator.maxIntegerLength = 1;
		validator.maxFractionLength = 1;
		assertTrue(validator.isValid(0.5, null));
		assertTrue(validator.isValid("0.5", null));
		assertTrue(validator.isValid(9.5, null));
		assertTrue(validator.isValid("9.5", null));
		assertFalse(validator.isValid(9.55, null));
		assertFalse(validator.isValid("9.55", null));
		assertFalse(validator.isValid(10, null));
		assertFalse(validator.isValid("10", null));
	}
}
