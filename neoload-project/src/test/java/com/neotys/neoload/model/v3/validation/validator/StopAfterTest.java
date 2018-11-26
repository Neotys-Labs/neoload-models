package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class StopAfterTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VALUE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or must be current_iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE = sb.toString();
	}

	private static final String CONSTRAINTS_TYPE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or must be current_iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_TYPE = sb.toString();
	}

	@Test
	public void validateValue() {
		final Validator validator = new Validator();
		
		StopAfter stopAfter = StopAfter.builder()
				.type(StopAfter.Type.TIME)
				.build();
		Validation validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(null)
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(-10)
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(0)
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(1)
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		stopAfter = StopAfter.builder()
				.value(1)
				.type(StopAfter.Type.CURRENT_ITERATION)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value("current_iteration")
				.type(StopAfter.Type.CURRENT_ITERATION)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.type(StopAfter.Type.CURRENT_ITERATION)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateType() {
		final Validator validator = new Validator();
		
		StopAfter stopAfter = StopAfter.builder()
				.value(1)
				.build();
		Validation validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(1)
				.type(null)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.type(StopAfter.Type.CURRENT_ITERATION)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		stopAfter = StopAfter.builder()
				.value(1)
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
