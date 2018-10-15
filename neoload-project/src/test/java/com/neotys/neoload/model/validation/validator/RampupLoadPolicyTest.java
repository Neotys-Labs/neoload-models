package com.neotys.neoload.model.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.validation.groups.NeoLoad;


public class RampupLoadPolicyTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_MIN_USERS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'min_users': must be greater than or equal to 1 user.").append(LINE_SEPARATOR);
		CONSTRAINTS_MIN_USERS = sb.toString();
	}

	private static final String CONSTRAINTS_MAX_USERS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'max_users': must be greater than or equal to 1 user.").append(LINE_SEPARATOR);
		CONSTRAINTS_MAX_USERS = sb.toString();
	}

	private static final String CONSTRAINTS_INCREMENT_USERS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'increment_users': must be greater than or equal to 1 user.").append(LINE_SEPARATOR);
		CONSTRAINTS_INCREMENT_USERS = sb.toString();
	}

	private static final String CONSTRAINTS_INCREMENT_EVERY;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'increment_every': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_INCREMENT_EVERY = sb.toString();
	}

	private static final String CONSTRAINTS_INCREMENT_RAMPUP;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'rampup': must be greater than or equal to 1 second.").append(LINE_SEPARATOR);
		CONSTRAINTS_INCREMENT_RAMPUP = sb.toString();
	}

	@Test
	public void validateMinUsers() {
		final Validator validator = new Validator();
		
		RampupLoadPolicy loadPolicy = RampupLoadPolicy.builder()
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MIN_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(-10)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MIN_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(0)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MIN_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateMaxUsers() {
		final Validator validator = new Validator();
		
		RampupLoadPolicy loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.maxUsers(-10)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MAX_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.maxUsers(0)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MAX_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.maxUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateIncrementUsers() {
		final Validator validator = new Validator();
		
		RampupLoadPolicy loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_INCREMENT_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(-10)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_INCREMENT_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(0)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_INCREMENT_USERS, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateIncrementEvery() {
		final Validator validator = new Validator();
		
		RampupLoadPolicy loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)				
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_INCREMENT_EVERY, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(null)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_INCREMENT_EVERY, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
	
	@Test
	public void validateIncrementRampup() {
		final Validator validator = new Validator();
		
		RampupLoadPolicy loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)				
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.rampup(-1)
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_INCREMENT_RAMPUP, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.rampup(0)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_INCREMENT_RAMPUP, validation.getMessage().get());	

		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		loadPolicy = RampupLoadPolicy.builder()
				.minUsers(1)
				.incrementUsers(1)
				.incrementEvery(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.rampup(1)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
}
