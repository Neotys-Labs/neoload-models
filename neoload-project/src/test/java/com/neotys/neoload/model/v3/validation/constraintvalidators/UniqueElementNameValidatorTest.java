package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueElementNameValidator;


public class UniqueElementNameValidatorTest {
	@Test
	public void isValid() {
		assertTrue(new UniqueElementNameValidator().isValid(null, null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(), null));
		
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Population.builder().build()), null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Population.builder().name("population").build()), null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Population.builder().name("population1").build(), Population.builder().name("population2").build()), null));

		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().build()), null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().name("scenario").build()), null));
		assertTrue(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().name("scenario1").build(), Scenario.builder().name("scenario2").build()), null));

		assertFalse(new UniqueElementNameValidator().isValid(Arrays.asList(Population.builder().name("population").build(), Population.builder().name("noitalupop").build(), Population.builder().name("population").build()), null));

		assertFalse(new UniqueElementNameValidator().isValid(Arrays.asList(Scenario.builder().name("scenario").build(), Scenario.builder().name("oiranecs").build(), Scenario.builder().name("scenario").build()), null));
	}
}
