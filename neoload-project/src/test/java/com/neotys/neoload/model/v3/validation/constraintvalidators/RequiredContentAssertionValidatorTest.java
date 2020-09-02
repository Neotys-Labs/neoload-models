package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;


public class RequiredContentAssertionValidatorTest {
	@Test
	public void isValid() {
		assertTrue(new RequiredContentAssertionValidator().isValid(null, null));
		
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().build(), null));
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().xPath("").jsonPath("").contains("").build(), null));
		
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().xPath("").build(), null));
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().xPath("").jsonPath("jsonpath").build(), null));
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().xPath("").contains("contains").build(), null));
		assertTrue(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().xPath("xpath").build(), null));
		
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().jsonPath("").build(), null));
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().jsonPath("").xPath("xpath").build(), null));
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().jsonPath("").contains("contains").build(), null));
		assertTrue(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().jsonPath("jsonpath").build(), null));
	
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().contains("").build(), null));
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().contains("").xPath("xpath").build(), null));
		assertFalse(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().contains("").jsonPath("contains").build(), null));
		assertTrue(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().contains("contains").build(), null));

		assertTrue(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().xPath("xpath").contains("contains").build(), null));
		assertTrue(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().jsonPath("jsonpath").contains("contains").build(), null));
		assertTrue(new RequiredContentAssertionValidator().isValid(ContentAssertion.builder().xPath("xpath").jsonPath("jsonpath").contains("contains").build(), null));
	}
}
