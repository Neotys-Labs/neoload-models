package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.time.Duration;
import org.junit.Test;

public class StartAfterTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VALUE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be between 1 and 2147483 seconds or must be an existing population.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE = sb.toString();
	}

	private static final String CONSTRAINTS_TYPE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be between 1 and 2147483 seconds or must be an existing population.").append(LINE_SEPARATOR);
		CONSTRAINTS_TYPE = sb.toString();
	}

	@Test
	public void validateValue() {
		final Validator validator = new Validator();
		
		StartAfter startAfter = StartAfter.builder()
				.type(StartAfter.Type.TIME)
				.build();
		Validation validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(null)
				.type(StartAfter.Type.TIME)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(null)
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(-10))
				.type(StartAfter.Type.TIME)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(-10))
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(0))
				.type(StartAfter.Type.TIME)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(0))
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(StartAfter.Type.TIME)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(" \t\r\n")
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value("My Population")
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}	

	@Test
	public void validateType() {
		final Validator validator = new Validator();
		
		StartAfter startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(1))
				.build();
		Validation validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(null)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		startAfter = StartAfter.builder()
				.value("My Population")
				.type(StartAfter.Type.POPULATION)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		startAfter = StartAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(StartAfter.Type.TIME)
				.build();
		validation = validator.validate(startAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
