package com.neotys.neoload.model.v3.validation.validator;


import com.neotys.neoload.model.v3.project.scenario.WhenRelease;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

import static org.junit.Assert.*;


public class WhenReleaseTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_VALUE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second.").append(LINE_SEPARATOR);
		CONSTRAINTS_VALUE = sb.toString();
	}

	private static final String CONSTRAINTS_TYPE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for '': must be greater than or equal to 1 second.").append(LINE_SEPARATOR);
		CONSTRAINTS_TYPE = sb.toString();
	}

	@Test
	public void validateValue() {
		final Validator validator = new Validator();
		
		WhenRelease whenRelease = WhenRelease.builder()
				.type(WhenRelease.Type.PERCENTAGE)
				.build();
		Validation validation = validator.validate(whenRelease, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());	

		whenRelease = WhenRelease.builder()
				.value(null)
				.type(WhenRelease.Type.PERCENTAGE)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());

		whenRelease = WhenRelease.builder()
				.value(-10)
				.type(WhenRelease.Type.PERCENTAGE)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());

		whenRelease = WhenRelease.builder()
				.value(0)
				.type(WhenRelease.Type.VU_NUMBER)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_VALUE, validation.getMessage().get());

		whenRelease = WhenRelease.builder()
				.value(1)
				.type(WhenRelease.Type.VU_NUMBER)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		whenRelease = WhenRelease.builder()
				.value("manualcc")
				.type(WhenRelease.Type.MANUAL)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertFalse(validation.isValid());

		whenRelease = WhenRelease.builder()
				.value("manual")
				.type(WhenRelease.Type.MANUAL)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertTrue(validation.isValid());
	}

	@Test
	public void validateType() {
		final Validator validator = new Validator();
		
		WhenRelease whenRelease = WhenRelease.builder()
				.value(1)
				.build();
		Validation validation = validator.validate(whenRelease, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		whenRelease = WhenRelease.builder()
				.value(1)
				.type(null)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_TYPE, validation.getMessage().get());	

		whenRelease = WhenRelease.builder()
				.type(WhenRelease.Type.PERCENTAGE)
				.value(10)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		whenRelease = WhenRelease.builder()
				.value(1)
				.type(WhenRelease.Type.VU_NUMBER)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());

		whenRelease = WhenRelease.builder()
				.value("manual")
				.type(WhenRelease.Type.MANUAL)
				.build();
		validation = validator.validate(whenRelease, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
