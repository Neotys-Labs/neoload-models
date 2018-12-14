package com.neotys.neoload.model.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.scenario.StopAfter;


public class StopAfterValidatorTest {
	@Test
	public void isValid() {
		assertFalse(new StopAfterValidator().isValid(null, null));
		
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().build(), null));
		
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.TIME).build(), null));
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.TIME).value("").build(), null));
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.TIME).value(-1).build(), null));
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.TIME).value(0).build(), null));
		assertTrue(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.TIME).value(1).build(), null));
		
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.CURRENT_ITERATION).value(0).build(), null));
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.CURRENT_ITERATION).value("").build(), null));
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.CURRENT_ITERATION).value(" \t\r\n").build(), null));
		assertFalse(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.CURRENT_ITERATION).value("value").build(), null));
		assertTrue(new StopAfterValidator().isValid(StopAfter.builder().type(StopAfter.Type.CURRENT_ITERATION).build(), null));		
	}
}
