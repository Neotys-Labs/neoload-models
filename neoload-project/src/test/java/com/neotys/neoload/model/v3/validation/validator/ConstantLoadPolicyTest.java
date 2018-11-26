package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class ConstantLoadPolicyTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_USERS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'users': must be greater than or equal to 1 user.").append(LINE_SEPARATOR);
		CONSTRAINTS_USERS = sb.toString();
	}

	@Test
	public void validateUsers() {
		final Validator validator = new Validator();
		
		ConstantLoadPolicy constantLoadPolicy = ConstantLoadPolicy.builder().build();
		Validation validation = validator.validate(constantLoadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USERS, validation.getMessage().get());	

		constantLoadPolicy = ConstantLoadPolicy.builder()
				.users(-10)
				.build();
		validation = validator.validate(constantLoadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USERS, validation.getMessage().get());	

		constantLoadPolicy = ConstantLoadPolicy.builder()
				.users(0)
				.build();
		validation = validator.validate(constantLoadPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_USERS, validation.getMessage().get());	

		constantLoadPolicy = ConstantLoadPolicy.builder()
				.users(1)
				.build();
		validation = validator.validate(constantLoadPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
}
