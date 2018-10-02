package com.neotys.neoload.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.scenario.ImmutableConstantLoadPolicy;
import com.neotys.neoload.model.scenario.ImmutableDuration;
import com.neotys.neoload.model.scenario.ImmutablePeakLoadPolicy;
import com.neotys.neoload.model.scenario.ImmutablePeaksLoadPolicy;
import com.neotys.neoload.model.scenario.ImmutablePopulationPolicy;
import com.neotys.neoload.model.scenario.ImmutableRampupLoadPolicy;
import com.neotys.neoload.model.scenario.ImmutableScenario;
import com.neotys.neoload.model.scenario.ImmutableStartAfter;
import com.neotys.neoload.model.scenario.ImmutableStopAfter;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy.Peak;
import com.neotys.neoload.model.scenario.PopulationPolicy;
import com.neotys.neoload.model.scenario.Scenario;
import com.neotys.neoload.model.scenario.StartAfter;
import com.neotys.neoload.model.scenario.StopAfter;


public class IOTest {
	private static Project getScenariosInLightVersion() throws IOException {
        final PopulationPolicy population11 = ImmutablePopulationPolicy.builder()
        		.name("MyPopulation11")
        		.loadPolicy(ImmutableConstantLoadPolicy.builder()
        				.users(500)
        				.build())
        		.build();
        
        final PopulationPolicy population12 = ImmutablePopulationPolicy.builder()
        		.name("MyPopulation12")
        		.loadPolicy(ImmutableRampupLoadPolicy.builder()
        				.minUsers(0)
        				.incrementUsers(10)
        				.incrementEvery(ImmutableDuration.builder()
								.value(5)
								.type(Duration.Type.TIME)
								.build())
        				.build())
        		.build();

        final PopulationPolicy population13 = ImmutablePopulationPolicy.builder()
        		.name("MyPopulation13")
        		.loadPolicy(ImmutablePeaksLoadPolicy.builder()
        				.minimum(ImmutablePeakLoadPolicy.builder()
        						.users(100)
        						.duration(ImmutableDuration.builder()
        								.value(120)
        								.type(Duration.Type.TIME)
        								.build())
        						.build())
        				.maximum(ImmutablePeakLoadPolicy.builder()
        						.users(500)
        						.duration(ImmutableDuration.builder()
        								.value(120)
        								.type(Duration.Type.TIME)
        								.build())
        						.build())
        				.start(Peak.MINIMUM)        				
        				.build())
        		.build();
  
        final Scenario scenario1 = ImmutableScenario.builder()
        		.name("MyScenario1")
        		.addPopulations(population11)
        		.addPopulations(population12)
        		.addPopulations(population13)
        		.build();
        
        final Project project = ImmutableProject.builder()
        		.name("MyProject")
        		.addScenarios(scenario1)
        		.build();
        
        return project;
	}
	
	private static Project getScenariosInFullVersion() throws IOException {
        final PopulationPolicy population11 = ImmutablePopulationPolicy.builder()
        		.name("MyPopulation11")
        		.loadPolicy(ImmutableConstantLoadPolicy.builder()
        				.users(500)
        				.duration(ImmutableDuration.builder()
								.value(900)
								.type(Duration.Type.TIME)
								.build())
        				.startAfter(ImmutableStartAfter.builder()
        						.value(30)
        						.type(StartAfter.Type.TIME)
        						.build())
        				.rampup(60)
        				.stopAfter(ImmutableStopAfter.builder()
        						.value(30)
        						.type(StopAfter.Type.TIME)
        						.build())
        				.build())
        		.build();
        
        final PopulationPolicy population12 = ImmutablePopulationPolicy.builder()
        		.name("MyPopulation12")
        		.loadPolicy(ImmutableRampupLoadPolicy.builder()
        				.minUsers(0)
        				.maxUsers(500)
        				.incrementUsers(10)
        				.incrementEvery(ImmutableDuration.builder()
								.value(1)
								.type(Duration.Type.ITERATION)
								.build())
        				.duration(ImmutableDuration.builder()
								.value(15)
								.type(Duration.Type.ITERATION)
								.build())
        				.startAfter(ImmutableStartAfter.builder()
        						.value("MyPopulation11")
        						.type(StartAfter.Type.POPULATION)
        						.build())
        				.rampup(90)
        				.stopAfter(ImmutableStopAfter.builder()
        						.type(StopAfter.Type.CURRENT_ITERATION)
        						.build())
        				.build())
        		.build();

        final PopulationPolicy population13 = ImmutablePopulationPolicy.builder()
        		.name("MyPopulation13")
        		.loadPolicy(ImmutablePeaksLoadPolicy.builder()
        				.minimum(ImmutablePeakLoadPolicy.builder()
        						.users(100)
        						.duration(ImmutableDuration.builder()
        								.value(1)
        								.type(Duration.Type.ITERATION)
        								.build())
        						.build())
        				.maximum(ImmutablePeakLoadPolicy.builder()
        						.users(500)
        						.duration(ImmutableDuration.builder()
        								.value(1)
        								.type(Duration.Type.ITERATION)
        								.build())
        						.build())
        				.start(Peak.MAXIMUM)
        				.duration(ImmutableDuration.builder()
								.value(15)
								.type(Duration.Type.ITERATION)
								.build())
        				.startAfter(ImmutableStartAfter.builder()
        						.value(60)
        						.type(StartAfter.Type.TIME)
        						.build())
        				.rampup(15)
        				.stopAfter(ImmutableStopAfter.builder()
        						.value(60)
        						.type(StopAfter.Type.TIME)
        						.build())
        				.build())
        		.build();
  
        final Scenario scenario1 = ImmutableScenario.builder()
        		.name("MyScenario1")
        		.description("My scenario 1 with 3 populations")
        		.slaProfile("MySlaProfile")
        		.addPopulations(population11)
        		.addPopulations(population12)
        		.addPopulations(population13)
        		.build();
        
        final Project project = ImmutableProject.builder()
        		.name("MyProject")
        		.addScenarios(scenario1)
        		.build();
        
        return project;
	}

	

	@Test
	public void readScenariosInLightVersion() throws IOException, URISyntaxException {
		final Project project = getScenariosInLightVersion();
		assertNotNull(project);
		
		readScenarios("test-scenarios-light-version", "yaml", project);
		readScenarios("test-scenarios-light-version", "json", project);
	}

	@Test
	public void readScenariosInFullVersion() throws IOException, URISyntaxException {
		final Project project = getScenariosInFullVersion();
		assertNotNull(project);
		
		readScenarios("test-scenarios-full-version", "yaml", project);
		readScenarios("test-scenarios-full-version", "json", project);
	}

	protected void readScenarios(final String fileName, final String extension, final Project expectedProject) throws IOException, URISyntaxException {
		final File file = new File(ClassLoader.getSystemResource(fileName + "." + extension).getFile());
		
		final IO<Project> mapper1 = new IO<Project>(Project.class);
		final Project actualProject1 = mapper1.read(file);
		assertEquals(expectedProject, actualProject1);
		
		final IO<Project> mapper2 = new IO<Project>(Project.class);
		final Project actualProject2 = mapper2.read(new String(Files.readAllBytes(Paths.get(file.toURI()))));
		assertEquals(expectedProject, actualProject2);
	}

	@Test
	public void writeScenariosInLightVersion() throws IOException, URISyntaxException {
		final Project project = getScenariosInLightVersion();
		assertNotNull(project);
		
		writeScenarios("test-scenarios-light-version", "yaml", project);
		writeScenarios("test-scenarios-light-version", "json", project);
	}

	@Test
	public void writeScenariosInFullVersion() throws IOException, URISyntaxException {
		final Project project = getScenariosInFullVersion();
		assertNotNull(project);
		
		writeScenarios("test-scenarios-full-version", "yaml", project);
		writeScenarios("test-scenarios-full-version", "json", project);
	}

	protected void writeScenarios(final String fileName, final String extension, final Project expectedProject) throws IOException, URISyntaxException {
		final File file = new File(ClassLoader.getSystemResource(fileName + "." + extension).getFile());
		
		final IO<Project> mapper = new IO<Project>(Project.class);

		String expectedContent = new String(Files.readAllBytes(Paths.get(file.toURI()))); 
		expectedContent = expectedContent.trim();
		expectedContent = expectedContent.replace("\r\n", "\n");
		
		final File actualFile = File.createTempFile(fileName + "-", "." + extension);
		mapper.write(actualFile, expectedProject);
		String actualContent = new String(Files.readAllBytes(Paths.get(actualFile.toURI())));
		assertNotNull(actualContent);
		actualContent = actualContent.trim();
		actualContent = actualContent.replace("\r\n", "\n");
		assertEquals(expectedContent, actualContent);			
		
		final Project actualProject = mapper.read(actualFile);
		assertNotNull(actualProject);
		assertEquals(expectedProject, actualProject);		
	}	
}
