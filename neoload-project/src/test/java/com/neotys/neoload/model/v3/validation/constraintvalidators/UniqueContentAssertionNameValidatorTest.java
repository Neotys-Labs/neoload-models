package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;


public class UniqueContentAssertionNameValidatorTest {
	@Test
	public void isValid() {
		assertTrue(new UniqueContentAssertionNameValidator().isValid(null, null));
		assertTrue(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(), null));
		
		assertTrue(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(ContentAssertion.builder().build()), null));
		assertTrue(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(ContentAssertion.builder().name("assertion").build()), null));
		assertTrue(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(ContentAssertion.builder().build(), ContentAssertion.builder().name("assertion").build()), null));
		assertTrue(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(ContentAssertion.builder().name("assertion1").build(), ContentAssertion.builder().name("assertion2").build()), null));
		assertTrue(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(ContentAssertion.builder().build(), ContentAssertion.builder().name("assertion1").build(), ContentAssertion.builder().name("assertion2").build()), null));

		assertFalse(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(ContentAssertion.builder().name("assertion").build(), ContentAssertion.builder().name("noitressa").build(), ContentAssertion.builder().name("assertion").build()), null));
		assertFalse(new UniqueContentAssertionNameValidator().isValid(Arrays.asList(ContentAssertion.builder().build(), ContentAssertion.builder().name("assertion").build(), ContentAssertion.builder().name("noitressa").build(), ContentAssertion.builder().name("assertion").build()), null));
	}
}
