package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.time.Duration;
import org.junit.Test;

public class StopAfterTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VALUE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be between 1 and 2147483 seconds or must be current_iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE = sb.toString();
	}

	private static String valueViolation(final String value) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value '" + value + "' for '': must be between 1 and 2147483 seconds or must be current_iteration.").append(LINE_SEPARATOR);
		return sb.toString();
	}

	private static String typeViolation(final String value) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value '" + value + "' for '': must be between 1 and 2147483 seconds or must be current_iteration.").append(LINE_SEPARATOR);
		return sb.toString();
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
				.value(Duration.ofSeconds(-10))
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(valueViolation("-10s"), validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(Duration.ofSeconds(0))
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(valueViolation("0"), validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		stopAfter = StopAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(StopAfter.Type.CURRENT_ITERATION)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(valueViolation("1s"), validation.getMessage().get());	

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
				.value(Duration.ofSeconds(1))
				.build();
		Validation validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(typeViolation("1s"), validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(null)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(typeViolation("1s"), validation.getMessage().get());	

		stopAfter = StopAfter.builder()
				.type(StopAfter.Type.CURRENT_ITERATION)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		stopAfter = StopAfter.builder()
				.value(Duration.ofSeconds(1))
				.type(StopAfter.Type.TIME)
				.build();
		validation = validator.validate(stopAfter, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
