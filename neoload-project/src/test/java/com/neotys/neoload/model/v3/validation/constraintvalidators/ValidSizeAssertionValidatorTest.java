package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;


public class ValidSizeAssertionValidatorTest {
	@Test
	public void isValid() {
		assertTrue(new ValidSizeAssertionValidator().isValid(null, null));

		assertFalse(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().build(), null));

		assertTrue(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().equals(1024L).build(), null));
		assertTrue(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().greaterThan(1024L).build(), null));
		assertTrue(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().lessThan(2048L).build(), null));
		assertTrue(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().greaterThan(1024L).lessThan(2048L).build(), null));

		assertFalse(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().equals(1024L).greaterThan(1L).build(), null));
		assertFalse(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().equals(1024L).lessThan(2048L).build(), null));
		assertFalse(new ValidSizeAssertionValidator().isValid(SizeAssertion.builder().equals(1024L).greaterThan(1L).lessThan(2048L).build(), null));
	}
}
