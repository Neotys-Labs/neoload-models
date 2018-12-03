package com.neotys.neoload.model.v3.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


public class ProjectTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_PROJECT_NAME;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value.").append(LINE_SEPARATOR);
		CONSTRAINTS_PROJECT_NAME = sb.toString();
	}

	private static final String CONSTRAINTS_PROJECT_USER_PATHS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'user_paths': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_PROJECT_USER_PATHS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_PROJECT_POPULATIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'populations': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_PROJECT_POPULATIONS_NAMES = sb.toString();
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
	public void validateUserPathsNames() {
		final Validator validator = new Validator();
		
		Project project = Project.builder()
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.name("actions")
								.addElements(Request.builder().url("url").build())
								.build())
						.build())
				.build();
        Validation validation = validator.validate(project, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		project = Project.builder()
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.addElements(Request.builder().url("url").build())
								.build())
						.build())
				.addUserPaths(UserPath.builder()
						.name("MyUserPath")
						.actions(Container.builder()
								.addElements(Request.builder().url("url").build())
								.build())
						.build())
				.build();
		validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_PROJECT_USER_PATHS_NAMES, validation.getMessage().get());	
	}

	@Test
	public void validatePopulationsNames() {
		final Validator validator = new Validator();
		
		Project project = Project.builder()
				.addPopulations(Population.builder()
						.name("MyPopulation")
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath")
								.distribution(100.0)
								.build())
						.build())
				.build();
        Validation validation = validator.validate(project, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
		
		project = Project.builder()
				.addPopulations(Population.builder()
						.name("MyPopulation")
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath")
								.distribution(100.0)
								.build())
						.build())
				.addPopulations(Population.builder()
						.name("MyPopulation")
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath")
								.distribution(100.0)
								.build())
						.build())
				.build();
		validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_PROJECT_POPULATIONS_NAMES, validation.getMessage().get());	
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
