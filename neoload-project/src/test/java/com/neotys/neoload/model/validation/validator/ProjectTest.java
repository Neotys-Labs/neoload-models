package com.neotys.neoload.model.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.PopulationPolicy;
import com.neotys.neoload.model.scenario.Scenario;
import com.neotys.neoload.model.validation.groups.NeoLoad;


public class ProjectTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_PROJECT_NAME;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_PROJECT_NAME = sb.toString();
	}

	private static final String CONSTRAINTS_PROJECT_SCENARIOS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'scenarios': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_PROJECT_SCENARIOS_NAMES = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		Project project = Project.builder().build();
		Validation validation = validator.validate(project, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		project = Project.builder().name("").build();
		validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_PROJECT_NAME, validation.getMessage().get());	

		project = Project.builder().name(" 	\r\t\n").build();
		validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_PROJECT_NAME, validation.getMessage().get());	

		project = Project.builder().name("My Project").build();
		validation = validator.validate(project, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validateScenariosNames() {
		final Validator validator = new Validator();
		
		Project project = Project.builder()
				.addScenarios(Scenario.builder()
						.name("MyScenario")
						.addPopulations(PopulationPolicy.builder()
								.name("MyPopulation")
								.loadPolicy(ConstantLoadPolicy.builder()
										.users(25)
										.build())
								.build())
						.build())
				.build();
        Validation validation = validator.validate(project, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		project = Project.builder()
				.addScenarios(Scenario.builder()
						.name("MyScenario")
						.addPopulations(PopulationPolicy.builder()
								.name("MyPopulation")
								.loadPolicy(ConstantLoadPolicy.builder()
										.users(25)
										.build())
								.build())
						.build())
				.addScenarios(Scenario.builder()
						.name("MyScenario")
						.addPopulations(PopulationPolicy.builder()
								.name("MyPopulation")
								.loadPolicy(ConstantLoadPolicy.builder()
										.users(25)
										.build())
								.build())
						.build())
				.build();
		validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_PROJECT_SCENARIOS_NAMES, validation.getMessage().get());	
	}
}
