package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class DurationTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VALUE_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE_NULL = sb.toString();
	}

	private static String valueMinViolation(final String value) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value '" + value + "' for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		return sb.toString();
	}

	private static String typeViolation(final String value) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value '" + value + "' for '': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		return sb.toString();
	}

	@Test
	public void validateValue() {
		final Validator validator = new Validator();
		
		LoadDuration duration = LoadDuration.builder()
				.type(LoadDuration.Type.TIME)
				.build();
		Validation validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = LoadDuration.builder()
				.type(LoadDuration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(null)
				.type(LoadDuration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(null)
				.type(LoadDuration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE_NULL, validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(-10)
				.type(LoadDuration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(valueMinViolation("-10"), validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(-10)
				.type(LoadDuration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(valueMinViolation("-10"), validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(0)
				.type(LoadDuration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(valueMinViolation("0"), validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(0)
				.type(LoadDuration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(valueMinViolation("0"), validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(1)
				.type(LoadDuration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		duration = LoadDuration.builder()
				.value(1)
				.type(LoadDuration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateType() {
		final Validator validator = new Validator();
		
		LoadDuration duration = LoadDuration.builder()
				.value(1)
				.build();
		Validation validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(typeViolation("1"), validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(1)
				.type(null)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(typeViolation("1"), validation.getMessage().get());	

		duration = LoadDuration.builder()
				.value(1)
				.type(LoadDuration.Type.ITERATION)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		duration = LoadDuration.builder()
				.value(1)
				.type(LoadDuration.Type.TIME)
				.build();
		validation = validator.validate(duration, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
