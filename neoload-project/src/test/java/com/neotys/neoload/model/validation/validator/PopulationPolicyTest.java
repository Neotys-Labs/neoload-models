package com.neotys.neoload.model.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.PopulationPolicy;
import com.neotys.neoload.model.validation.groups.NeoLoad;


public class PopulationPolicyTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_POPULATION_POLICY_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_POPULATION_POLICY_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_POPULATION_POLICY_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_POPULATION_POLICY_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_POPULATION_POLICY_LOAD_POLICY;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'load_policy': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_POPULATION_POLICY_LOAD_POLICY = sb.toString();
	}


	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		PopulationPolicy populationPolicy = PopulationPolicy.builder()
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(25)
						.build())
				.build();
		Validation validation = validator.validate(populationPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_POLICY_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		populationPolicy = PopulationPolicy.builder()
				.name("")
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(25)
						.build())
				.build();
		validation = validator.validate(populationPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_POLICY_NAME_BLANK, validation.getMessage().get());	

		populationPolicy = PopulationPolicy.builder()
				.name(" 	\r\t\n")
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(25)
						.build())
				.build();
		validation = validator.validate(populationPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_POLICY_NAME_BLANK, validation.getMessage().get());	

		populationPolicy = PopulationPolicy.builder()
				.name("MyPopulation")
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(25)
						.build())
				.build();
		validation = validator.validate(populationPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateLoadPolicy() {
		final Validator validator = new Validator();
				
		PopulationPolicy populationPolicy = PopulationPolicy.builder()
				.name("MyPopulation")
				.build();
		Validation validation = validator.validate(populationPolicy, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_POLICY_LOAD_POLICY, validation.getMessage().get());	

		populationPolicy = PopulationPolicy.builder()
				.name("MyPopulation")
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(25)
						.build())
				.build();
		validation = validator.validate(populationPolicy, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
}
