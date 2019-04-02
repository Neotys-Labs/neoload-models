package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class PeaksLoadPolicyTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_MINIMUM;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'minimum': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_MINIMUM = sb.toString();
	}

	private static final String CONSTRAINTS_MAXIMUM;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'maximum': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_MAXIMUM = sb.toString();
	}

	private static final String CONSTRAINTS_START;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'start': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_START = sb.toString();
	}

	@Test
	public void validateMinimum() {
		final Validator validator = new Validator();
		
		PeaksLoadPolicy loadPolicy = PeaksLoadPolicy.builder()
				.maximum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.start(PeaksLoadPolicy.Peak.MINIMUM)
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MINIMUM, validation.getMessage().get());	

		loadPolicy = PeaksLoadPolicy.builder()
				.minimum(null)
				.maximum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.start(PeaksLoadPolicy.Peak.MINIMUM)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MINIMUM, validation.getMessage().get());	

		loadPolicy = PeaksLoadPolicy.builder()
				.minimum(PeakLoadPolicy.builder()
						.users(1)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.maximum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.start(PeaksLoadPolicy.Peak.MINIMUM)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateMaximum() {
		final Validator validator = new Validator();
		
		PeaksLoadPolicy loadPolicy = PeaksLoadPolicy.builder()
				.minimum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.start(PeaksLoadPolicy.Peak.MINIMUM)
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MAXIMUM, validation.getMessage().get());	

		loadPolicy = PeaksLoadPolicy.builder()
				.minimum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.maximum(null)
				.start(PeaksLoadPolicy.Peak.MINIMUM)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_MAXIMUM, validation.getMessage().get());	

		loadPolicy = PeaksLoadPolicy.builder()
				.minimum(PeakLoadPolicy.builder()
						.users(1)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.maximum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.start(PeaksLoadPolicy.Peak.MINIMUM)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateStart() {
		final Validator validator = new Validator();
		
		PeaksLoadPolicy loadPolicy = PeaksLoadPolicy.builder()
				.minimum(PeakLoadPolicy.builder()
						.users(1)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.maximum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.build();
		Validation validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_START, validation.getMessage().get());	

		loadPolicy = PeaksLoadPolicy.builder()
				.minimum(PeakLoadPolicy.builder()
						.users(1)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.maximum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.start(null)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_START, validation.getMessage().get());	

		loadPolicy = PeaksLoadPolicy.builder()
				.minimum(PeakLoadPolicy.builder()
						.users(1)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.maximum(PeakLoadPolicy.builder()
						.users(10)
						.duration(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.start(PeaksLoadPolicy.Peak.MINIMUM)
				.build();
		validation = validator.validate(loadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
