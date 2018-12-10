package com.neotys.neoload.model.v3.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.constraintvalidators.DurationValidator;


public class DurationValidatorTest {
	@Test
	public void isValid() {
		assertFalse(new DurationValidator().isValid(null, null));
		
		assertFalse(new DurationValidator().isValid(LoadDuration.builder().build(), null));
		
		assertFalse(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.TIME).build(), null));
		assertFalse(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.ITERATION).build(), null));
		
		assertFalse(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.TIME).value(-1).build(), null));
		assertFalse(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.ITERATION).value(-1).build(), null));
		
		assertFalse(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.TIME).value(0).build(), null));
		assertFalse(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.ITERATION).value(0).build(), null));
		
		assertTrue(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.TIME).value(1).build(), null));
		assertTrue(new DurationValidator().isValid(LoadDuration.builder().type(LoadDuration.Type.ITERATION).value(1).build(), null));
	}
}
