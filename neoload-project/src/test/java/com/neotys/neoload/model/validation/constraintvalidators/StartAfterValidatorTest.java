package com.neotys.neoload.model.validation.constraintvalidators;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.scenario.StartAfter;


public class StartAfterValidatorTest {
	@Test
	public void isValid() {
		assertFalse(new StartAfterValidator().isValid(null, null));
		
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().build(), null));
		
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.TIME).build(), null));
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.POPULATION).build(), null));
		
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.TIME).value("").build(), null));
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.TIME).value(-1).build(), null));
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.TIME).value(0).build(), null));
		assertTrue(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.TIME).value(1).build(), null));
		
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.POPULATION).value(0).build(), null));
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.POPULATION).value("").build(), null));
		assertFalse(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.POPULATION).value(" \t\r\n").build(), null));
		assertTrue(new StartAfterValidator().isValid(StartAfter.builder().type(StartAfter.Type.POPULATION).value("value").build(), null));
	}
}
