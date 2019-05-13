package com.neotys.neoload.model.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.io.IO.Format;
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


public class IOTest {
	private static Project getScenariosInLightVersion() throws IOException {
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
	
	private static Project getScenariosInFullVersion() throws IOException {
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
		
		final IO mapper1 = new IO();
		final Project actualProject1 = mapper1.read(file, Project.class);
		assertEquals(expectedProject, actualProject1);
		
		final IO mapper2 = new IO();
		final Project actualProject2 = mapper2.read(new String(Files.readAllBytes(Paths.get(file.toURI()))), Project.class);
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
		
		final IO mapper = new IO();

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
		
		final Project actualProject = mapper.read(actualFile, Project.class);
		assertNotNull(actualProject);
		assertEquals(expectedProject, actualProject);		
	}	
	
	@Test
	public void getFormatFromFile() {
		final IO mapper = new IO();
		
		assertNull(mapper.getFormat((File)null));
		
		try {
			mapper.getFormat(new File("./project"));
			fail("The extension of the file must be 'yaml' or 'json'");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The extension of the file must be 'yaml' or 'json'", iae.getMessage());
		}

		try {
			mapper.getFormat(new File("./project.yml"));
			fail("The extension of the file must be 'yaml' or 'json'");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The extension of the file must be 'yaml' or 'json'", iae.getMessage());
		}
		
		assertEquals(Format.YAML, mapper.getFormat(new File("./project.yaml")));
		assertEquals(Format.JSON, mapper.getFormat(new File("./project.json")));
	}

	@Test
	public void getFormatFromString() {
		final IO mapper = new IO();
		
		assertNull(mapper.getFormat((String)null));
		assertNull(mapper.getFormat(""));
		assertNull(mapper.getFormat(" \t\r\n"));
		
		assertEquals(Format.YAML, mapper.getFormat(" \t\r\n--- \t\r\n"));
		assertEquals(Format.YAML, mapper.getFormat("---"));
		assertEquals(Format.YAML, mapper.getFormat(" \t\r\nscenarios: \t\r\n"));
		assertEquals(Format.YAML, mapper.getFormat("scenarios:"));
		
		assertEquals(Format.JSON, mapper.getFormat(" \t\r\n{scenarios:[]} \t\r\n"));
		assertEquals(Format.JSON, mapper.getFormat("{scenarios:[]}"));
	}

	@Test
	public void getMapper() {
		final IO mapper = new IO();
		
		try {
			mapper.getMapper(null);
			fail("The format is unknown");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The format is unknown", iae.getMessage());
		}
		
		
		assertEquals(YAMLMapper.class, mapper.getMapper(Format.YAML).getClass());
		assertEquals(ObjectMapper.class, mapper.getMapper(Format.JSON).getClass());
	}
}
