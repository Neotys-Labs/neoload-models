package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.Duration;
import com.neotys.neoload.model.v3.project.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class PeakLoadPolicyTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_USERS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'users': must be greater than or equal to 1 user.").append(LINE_SEPARATOR);
		CONSTRAINTS_USERS = sb.toString();
	}

	private static final String CONSTRAINTS_DURATION;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'duration': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_DURATION = sb.toString();
	}

	@Test
	public void validateUsers() {
		final Validator validator = new Validator();
		
		PeakLoadPolicy loadPolicy = PeakLoadPolicy.builder()
				.duration(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USERS, validation.getMessage().get());	

		loadPolicy = PeakLoadPolicy.builder()
				.users(-10)
				.duration(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USERS, validation.getMessage().get());	

		loadPolicy = PeakLoadPolicy.builder()
				.users(0)
				.duration(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USERS, validation.getMessage().get());	

		loadPolicy = PeakLoadPolicy.builder()
				.users(1)
				.duration(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateDuration() {
		final Validator validator = new Validator();
		
		PeakLoadPolicy loadPolicy = PeakLoadPolicy.builder()
				.users(1)
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DURATION, validation.getMessage().get());	

		loadPolicy = PeakLoadPolicy.builder()
				.users(1)
				.duration(null)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DURATION, validation.getMessage().get());	

		loadPolicy = PeakLoadPolicy.builder()
				.users(1)
				.duration(Duration.builder()
						.value(1)
						.type(Duration.Type.TIME)
						.build())
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
