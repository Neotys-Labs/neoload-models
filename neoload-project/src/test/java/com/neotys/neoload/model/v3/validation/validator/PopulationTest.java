package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class PopulationTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_POPULATION_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_POPULATION_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_POPULATION_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_POPULATION_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_POPULATION_USERPATHS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'user_paths': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_POPULATION_USERPATHS = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		Population population = Population.builder()
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.distribution(100.0)
						.build())
				.build();
		Validation validation = validator.validate(population, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		population = Population.builder()
				.name("")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.distribution(100.0)
						.build())
				.build();
		validation = validator.validate(population, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_NAME_BLANK, validation.getMessage().get());	

		population = Population.builder()
				.name(" 	\r\t\n")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.distribution(100.0)
						.build())
				.build();
		validation = validator.validate(population, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_NAME_BLANK, validation.getMessage().get());	

		population = Population.builder()
				.name("MyPopulation")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.distribution(100.0)
						.build())
				.build();
		validation = validator.validate(population, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	

	@Test
	public void validateUserPaths() {
		final Validator validator = new Validator();
		
		Population population = Population.builder()
				.name("MyPopulation")
				.build();
		Validation validation = validator.validate(population, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_POPULATION_USERPATHS, validation.getMessage().get());	

		population = Population.builder()
				.name("MyPopulation")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.distribution(100.0)
						.build())
				.build();
		validation = validator.validate(population, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
	
	@Test
	public void validateUserPathsNames() {
		final Validator validator = new Validator();
				
		Population population = Population.builder()
				.name("MyPopulation")
				.description("MyDescription")
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.distribution(50.0)
						.build())
				.addUserPaths(UserPathPolicy.builder()
						.name("MyUserPath")
						.distribution(50.0)
						.build())
				.build();
		Validation validation = validator.validate(population, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
}
