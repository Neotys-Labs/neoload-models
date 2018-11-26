package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.Duration;
import com.neotys.neoload.model.v3.project.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy.Peak;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;


public class IOUserPathsTest extends AbstractIOElementsTest {
	private static Project getUserPathsOnlyRequired() throws IOException {
        final PopulationPolicy population11 = PopulationPolicy.builder()
        		.name("MyPopulation11")
        		.loadPolicy(ConstantLoadPolicy.builder()
        				.users(500)
        				.build())
        		.build();
        
        final PopulationPolicy population12 = PopulationPolicy.builder()
        		.name("MyPopulation12")
        		.loadPolicy(RampupLoadPolicy.builder()
        				.minUsers(0)
        				.incrementUsers(10)
        				.incrementEvery(Duration.builder()
								.value(5)
								.type(Duration.Type.TIME)
								.build())
        				.build())
        		.build();

        final PopulationPolicy population13 = PopulationPolicy.builder()
        		.name("MyPopulation13")
        		.loadPolicy(PeaksLoadPolicy.builder()
        				.minimum(PeakLoadPolicy.builder()
        						.users(100)
        						.duration(Duration.builder()
        								.value(120)
        								.type(Duration.Type.TIME)
        								.build())
        						.build())
        				.maximum(PeakLoadPolicy.builder()
        						.users(500)
        						.duration(Duration.builder()
        								.value(120)
        								.type(Duration.Type.TIME)
        								.build())
        						.build())
        				.start(Peak.MINIMUM)        				
        				.build())
        		.build();
  
        final Scenario scenario1 = Scenario.builder()
        		.name("MyScenario1")
        		.addPopulations(population11)
        		.addPopulations(population12)
        		.addPopulations(population13)
        		.build();
        
        final Project project = Project.builder()
        		.name("MyProject")
        		.addScenarios(scenario1)
        		.build();
        
        return project;
	}
	
	private static Project getUserPathsRequiredAndOptional() throws IOException {
        final PopulationPolicy population11 = PopulationPolicy.builder()
        		.name("MyPopulation11")
        		.loadPolicy(ConstantLoadPolicy.builder()
        				.users(500)
        				.duration(Duration.builder()
								.value(900)
								.type(Duration.Type.TIME)
								.build())
        				.startAfter(StartAfter.builder()
        						.value(30)
        						.type(StartAfter.Type.TIME)
        						.build())
        				.rampup(60)
        				.stopAfter(StopAfter.builder()
        						.value(30)
        						.type(StopAfter.Type.TIME)
        						.build())
        				.build())
        		.build();
        
        final PopulationPolicy population12 = PopulationPolicy.builder()
        		.name("MyPopulation12")
        		.loadPolicy(RampupLoadPolicy.builder()
        				.minUsers(0)
        				.maxUsers(500)
        				.incrementUsers(10)
        				.incrementEvery(Duration.builder()
								.value(1)
								.type(Duration.Type.ITERATION)
								.build())
        				.duration(Duration.builder()
								.value(15)
								.type(Duration.Type.ITERATION)
								.build())
        				.startAfter(StartAfter.builder()
        						.value("MyPopulation11")
        						.type(StartAfter.Type.POPULATION)
        						.build())
        				.rampup(90)
        				.stopAfter(StopAfter.builder()
        						.type(StopAfter.Type.CURRENT_ITERATION)
        						.build())
        				.build())
        		.build();

        final PopulationPolicy population13 = PopulationPolicy.builder()
        		.name("MyPopulation13")
        		.loadPolicy(PeaksLoadPolicy.builder()
        				.minimum(PeakLoadPolicy.builder()
        						.users(100)
        						.duration(Duration.builder()
        								.value(1)
        								.type(Duration.Type.ITERATION)
        								.build())
        						.build())
        				.maximum(PeakLoadPolicy.builder()
        						.users(500)
        						.duration(Duration.builder()
        								.value(1)
        								.type(Duration.Type.ITERATION)
        								.build())
        						.build())
        				.start(Peak.MAXIMUM)
        				.duration(Duration.builder()
								.value(15)
								.type(Duration.Type.ITERATION)
								.build())
        				.startAfter(StartAfter.builder()
        						.value(60)
        						.type(StartAfter.Type.TIME)
        						.build())
        				.rampup(15)
        				.stopAfter(StopAfter.builder()
        						.value(60)
        						.type(StopAfter.Type.TIME)
        						.build())
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
        
        return project;
	}

	@Test
	public void readUserPathsOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProject = getUserPathsOnlyRequired();
		assertNotNull(expectedProject);
		
		read("test-userpaths-only-required", expectedProject);
		read("test-userpaths-only-required", expectedProject);
	}

	@Test
	public void readUserPathsRequiredAndOptional() throws IOException, URISyntaxException {
		final Project expectedProject = getUserPathsRequiredAndOptional();
		assertNotNull(expectedProject);
		
		read("test-userpaths-required-and-optional", expectedProject);
		read("test-userpaths-required-and-optional", expectedProject);
	}

	@Test
	public void writeUserPathsOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProject = getUserPathsOnlyRequired();
		assertNotNull(expectedProject);
		
		write("test-userpaths-only-required", expectedProject);
		write("test-userpaths-only-required", expectedProject);
	}

	@Test
	public void writeUserPathsRequiredAndOptional() throws IOException, URISyntaxException {
		final Project expectedProject = getUserPathsRequiredAndOptional();
		assertNotNull(expectedProject);
		
		write("test-userpaths-required-and-optional", expectedProject);
		write("test-userpaths-required-and-optional", expectedProject);
	}
}
