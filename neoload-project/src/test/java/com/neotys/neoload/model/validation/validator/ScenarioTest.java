package com.neotys.neoload.model.validation.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy.Peak;
import com.neotys.neoload.model.scenario.PopulationPolicy;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.scenario.Scenario;
import com.neotys.neoload.model.scenario.StartAfter;
import com.neotys.neoload.model.scenario.StopAfter;
import com.neotys.neoload.model.validation.groups.NeoLoad;


public class ScenarioTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String CONSTRAINTS_SCENARIO_NAME_BLANK_AND_NULL;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SCENARIO_NAME_BLANK_AND_NULL = sb.toString();
	}

	private static final String CONSTRAINTS_SCENARIO_NAME_BLANK;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'name': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SCENARIO_NAME_BLANK = sb.toString();
	}

	private static final String CONSTRAINTS_SCENARIO_POPULATIONS;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'populations': missing value or value is empty.").append(LINE_SEPARATOR);
		CONSTRAINTS_SCENARIO_POPULATIONS = sb.toString();
	}

	private static final String CONSTRAINTS_SCENARIO_POPULATIONS_NAMES;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 1.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'populations': must contain only unique names.").append(LINE_SEPARATOR);
		CONSTRAINTS_SCENARIO_POPULATIONS_NAMES = sb.toString();
	}

	private static final String CONSTRAINTS_FULL_VERSION;
	static {
		final StringBuilder sb = new StringBuilder();
		sb.append("Data Model is invalid. Violation Number: 12.").append(LINE_SEPARATOR);
		sb.append("Violation 1 - Incorrect value for 'scenarios[0].populations[0].constant_load.duration': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 2 - Incorrect value for 'scenarios[0].populations[0].constant_load.start_after': must be greater than or equal to 1 second or must be an existing population.").append(LINE_SEPARATOR);
		sb.append("Violation 3 - Incorrect value for 'scenarios[0].populations[0].constant_load.stop_after': must be greater than or equal to 1 second or must be current_iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 4 - Incorrect value for 'scenarios[0].populations[1].rampup_load.duration': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 5 - Incorrect value for 'scenarios[0].populations[1].rampup_load.increment_every': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 6 - Incorrect value for 'scenarios[0].populations[1].rampup_load.start_after': must be greater than or equal to 1 second or must be an existing population.").append(LINE_SEPARATOR);
		sb.append("Violation 7 - Incorrect value for 'scenarios[0].populations[1].rampup_load.stop_after': must be greater than or equal to 1 second or must be current_iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 8 - Incorrect value for 'scenarios[0].populations[2].peaks_load.duration': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 9 - Incorrect value for 'scenarios[0].populations[2].peaks_load.maximum.duration': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 10 - Incorrect value for 'scenarios[0].populations[2].peaks_load.minimum.duration': must be greater than or equal to 1 second or 1 iteration.").append(LINE_SEPARATOR);
		sb.append("Violation 11 - Incorrect value for 'scenarios[0].populations[2].peaks_load.start_after': must be greater than or equal to 1 second or must be an existing population.").append(LINE_SEPARATOR);
		sb.append("Violation 12 - Incorrect value for 'scenarios[0].populations[2].peaks_load.stop_after': must be greater than or equal to 1 second or must be current_iteration.").append(LINE_SEPARATOR);
		CONSTRAINTS_FULL_VERSION = sb.toString();
	}

	@Test
	public void validateName() {
		final Validator validator = new Validator();
		
		Scenario scenario = Scenario.builder()
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.build();
		Validation validation = validator.validate(scenario, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SCENARIO_NAME_BLANK_AND_NULL, validation.getMessage().get());	

		scenario = Scenario.builder()
				.name("")
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.build();
		validation = validator.validate(scenario, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SCENARIO_NAME_BLANK, validation.getMessage().get());	

		scenario = Scenario.builder()
				.name(" 	\r\t\n")
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.build();
		validation = validator.validate(scenario, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SCENARIO_NAME_BLANK, validation.getMessage().get());	

		scenario = Scenario.builder()
				.name("MyScenario")
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.build();
		validation = validator.validate(scenario, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}	
	
	@Test
	public void validatePopulations() {
		final Validator validator = new Validator();
		
		Scenario scenario = Scenario.builder()
				.name("MyScenario")
				.build();
		Validation validation = validator.validate(scenario, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SCENARIO_POPULATIONS, validation.getMessage().get());	

		scenario = Scenario.builder()
				.name("MyScenario")
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.build();
		validation = validator.validate(scenario, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
	
	@Test
	public void validatePopulationsNames() {
		final Validator validator = new Validator();
				
		Scenario scenario = Scenario.builder()
				.name("MyScenario")
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.build();
		Validation validation = validator.validate(scenario, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_SCENARIO_POPULATIONS_NAMES, validation.getMessage().get());	

		scenario = Scenario.builder()
				.name("MyScenario")
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation1")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.addPopulations(PopulationPolicy.builder()
						.name("MyPopulation2")
						.loadPolicy(ConstantLoadPolicy.builder()
								.users(25)
								.build())
						.build())
				.build();
	    validation = validator.validate(scenario, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());	
	}
	
	@Test
	public void validateFullVersion() {
		final Validator validator = new Validator();
		
        final PopulationPolicy population11 = PopulationPolicy.builder()
        		.name("MyPopulation11")
        		.loadPolicy(ConstantLoadPolicy.builder()
        				.users(500)
        				.duration(Duration.builder().build())
        				.startAfter(StartAfter.builder().build())
        				.rampup(60)
        				.stopAfter(StopAfter.builder().build())
        				.build())
        		.build();
        
        final PopulationPolicy population12 = PopulationPolicy.builder()
        		.name("MyPopulation12")
        		.loadPolicy(RampupLoadPolicy.builder()
        				.minUsers(10)
        				.maxUsers(500)
        				.incrementUsers(10)
        				.incrementEvery(Duration.builder().build())
        				.duration(Duration.builder().build())
        				.startAfter(StartAfter.builder().build())
        				.rampup(90)
        				.stopAfter(StopAfter.builder().build())
        				.build())
        		.build();

        final PopulationPolicy population13 = PopulationPolicy.builder()
        		.name("MyPopulation13")
        		.loadPolicy(PeaksLoadPolicy.builder()
        				.minimum(PeakLoadPolicy.builder()
        						.users(100)
        						.duration(Duration.builder().build())
        						.build())
        				.maximum(PeakLoadPolicy.builder()
        						.users(500)
        						.duration(Duration.builder().build())
        						.build())
        				.start(Peak.MAXIMUM)
        				.duration(Duration.builder().build())
        				.startAfter(StartAfter.builder().build())
        				.rampup(15)
        				.stopAfter(StopAfter.builder().build())
        				.build())
        		.build();
  
        final Scenario scenario1 = Scenario.builder()
        		.name("MyScenario1")
        		.description("My scenario 1 with 3 populations")
        		.slaProfile("MySlaProfile")
        		.addPopulations(population11)
        		.addPopulations(population12)
        		.addPopulations(population13)
        		.build();
        
        final Project project = Project.builder()
        		.name("MyProject")
        		.addScenarios(scenario1)
        		.build();
        
		Validation validation = validator.validate(project, NeoLoad.class);
		assertFalse(validation.isValid());
		assertEquals(CONSTRAINTS_FULL_VERSION, validation.getMessage().get());	
	}	
}
