package com.neotys.neoload.model.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.neotys.neoload.model.scenario.Scenario;


public class UniqueElementNameValidatorTest {
	@Test
	public void isValid() {
		assertTrue(new UniqueElementNameValidator().isValid(null, null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(), null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().build()), null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().name("scenario").build()), null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().name("scenario1").build(), Scenario.builder().name("scenario2").build()), null));

		assertFalse(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().name("scenario").build(), Scenario.builder().name("oiranecs").build(), Scenario.builder().name("scenario").build()), null));
	}
}
