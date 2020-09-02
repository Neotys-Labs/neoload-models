package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;


public class UniqueContentAssertionPathValidatorTest {
	@Test
	public void isValid() {
		assertTrue(new UniqueContentAssertionPathValidator().isValid(null, null));
		
		assertTrue(new UniqueContentAssertionPathValidator().isValid(ContentAssertion.builder().build(), null));
		assertTrue(new UniqueContentAssertionPathValidator().isValid(ContentAssertion.builder().xPath("xpath").build(), null));
		assertTrue(new UniqueContentAssertionPathValidator().isValid(ContentAssertion.builder().jsonPath("jsonpath").build(), null));
		
		assertFalse(new UniqueContentAssertionPathValidator().isValid(ContentAssertion.builder().xPath("xpath").jsonPath("jsonpath").build(), null));
	}
}
