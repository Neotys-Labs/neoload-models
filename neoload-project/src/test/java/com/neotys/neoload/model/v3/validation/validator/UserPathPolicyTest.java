package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class UserPathPolicyTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_DISTRIBUTION_RANGE;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'distribution': must be between 0 and 100.").append(LINE_SEPARATOR);
		CONSTRAINTS_DISTRIBUTION_RANGE = sb.toString();
	}

	private static final String CONSTRAINTS_DISTRIBUTION_DIGITS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'distribution': numeric value out of bounds (<3 digits>.<1 digits> expected).").append(LINE_SEPARATOR);
		CONSTRAINTS_DISTRIBUTION_DIGITS = sb.toString();
	}

	private static final String CONSTRAINTS_DISTRIBUTION_RANGE_AND_DIGITS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 2.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'distribution': must be between 0 and 100.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'distribution': numeric value out of bounds (<3 digits>.<1 digits> expected).").append(LINE_SEPARATOR);
		CONSTRAINTS_DISTRIBUTION_RANGE_AND_DIGITS = sb.toString();
	}


	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		UserPathPolicy userPathPolicy = UserPathPolicy.builder()
				.distribution(100.0)
				.build();
		Validation validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		userPathPolicy = UserPathPolicy.builder()
				.name("")
				.distribution(100.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_NAME_BLANK, validation.getMessage().get());	

		userPathPolicy = UserPathPolicy.builder()
				.name(" 	\r\t\n")
				.distribution(100.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_NAME_BLANK, validation.getMessage().get());	

		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(100.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateDistribution() {
		final Validator validator = new Validator();
				
		UserPathPolicy userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.build();
		Validation validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		
		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(-1.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DISTRIBUTION_RANGE, validation.getMessage().get());	

		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(101.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DISTRIBUTION_RANGE, validation.getMessage().get());	

		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(0.15)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DISTRIBUTION_DIGITS, validation.getMessage().get());	

		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(1000.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_DISTRIBUTION_RANGE_AND_DIGITS, validation.getMessage().get());	
		
		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(0.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(50.5)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	

		userPathPolicy = UserPathPolicy.builder()
				.name("MyUserPath")
				.distribution(100.0)
				.build();
		validation = validator.validate(userPathPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
	}
}
