package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.Duration;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class DurationTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VALUE_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_VALUE_MIN;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE_MIN = sb.toString();
	}

	private static final String CONSTRAINTS_TYPE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_TYPE = sb.toString();
	}

	@Test
	public void validateValue() {
		final Validator validator = new Validator();
		
		Duration duration = Duration.builder()
				.type(Duration.Type.TIME)
				.build();
		Validation validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = Duration.builder()
				.type(Duration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = Duration.builder()
				.value(null)
				.type(Duration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = Duration.builder()
				.value(null)
				.type(Duration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = Duration.builder()
				.value(-10)
				.type(Duration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = Duration.builder()
				.value(-10)
				.type(Duration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = Duration.builder()
				.value(0)
				.type(Duration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = Duration.builder()
				.value(0)
				.type(Duration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_MIN, validation.getMessage().get());	

		duration = Duration.builder()
				.value(1)
				.type(Duration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		duration = Duration.builder()
				.value(1)
				.type(Duration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateType() {
		final Validator validator = new Validator();
		
		Duration duration = Duration.builder()
				.value(1)
				.build();
		Validation validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		duration = Duration.builder()
				.value(1)
				.type(null)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		duration = Duration.builder()
				.value(1)
				.type(Duration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		duration = Duration.builder()
				.value(1)
				.type(Duration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
