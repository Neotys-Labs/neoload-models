package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;


public class IOTest extends AbstractIOElementsTest {
	private static final String EXPECTED_PROJECT_NAME = "MyéèProject";
	private static final String XPECTED_SCENARIO_NAME = "MyéèScenario";
	private static final String XPECTED_POPULATION_NAME = "MyéèPopulation";	
	
	private static ProjectDescriptor getProjetDescriptorWithScenarios() {
		final PopulationPolicy population = PopulationPolicy.builder()
				.name(XPECTED_POPULATION_NAME)
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(500)
						.build())
				.build();

		final Scenario scenario = Scenario.builder()
				.name(XPECTED_SCENARIO_NAME)
				.addPopulations(population)
				.isStoredVariables(true)
				.build();

		final Project project = Project.builder()
				.name(EXPECTED_PROJECT_NAME)
				.addScenarios(scenario)
				.build();

		return ProjectDescriptor.builder().project(project).build();
	}

	@Test
	public void getFormatFromFile() {
		final IO mapper = new IO();
		
		assertNull(mapper.getFormat((File)null));
		
		try {
			mapper.getFormat(new File("./project"));
			fail("The extension of the file must be 'yaml', 'yml' or 'json'");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The extension of the file must be 'yaml', 'yml' or 'json'", iae.getMessage());
		}

		try {
			mapper.getFormat(new File("./project.txt"));
			fail("The extension of the file must be 'yaml', 'yml' or 'json'");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The extension of the file must be 'yaml', 'yml' or 'json'", iae.getMessage());
		}

		assertEquals(Format.YAML, mapper.getFormat(new File("./project.yaml")));
		assertEquals(Format.YAML, mapper.getFormat(new File("./project.yml")));
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
	
	@Test
	public void read() throws IOException {
		final ProjectDescriptor expectedDescriptor = getProjetDescriptorWithScenarios();
		assertNotNull(expectedDescriptor);
				
		readAndCheck("test-scenarios-iso-8859-1", StandardCharsets.ISO_8859_1, StandardCharsets.ISO_8859_1, expectedDescriptor);
		readAndCheck("test-scenarios-iso-8859-1", StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8, expectedDescriptor);
		
		readAndCheck("test-scenarios-utf-8", StandardCharsets.UTF_8, StandardCharsets.UTF_8, expectedDescriptor);		
		readAndCheck("test-scenarios-utf-8", StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, expectedDescriptor);
	}

	private void readAndCheck(final String fileName, final Charset fileCharset, final Charset useCharset, final ProjectDescriptor expectedDescriptor) throws IOException {
		readAndCheck(fileName, "yaml", fileCharset, useCharset, expectedDescriptor);
		readAndCheck(fileName, "json", fileCharset, useCharset, expectedDescriptor);
	}

	private void readAndCheck(final String fileName, final String extension, final Charset fileCharset, final Charset useCharset, final ProjectDescriptor expectedDescriptor) throws IOException {
		final File file = getFile(fileName, extension);
		final IO mapper1 = new IO();
		final ProjectDescriptor actualDescriptor1 = mapper1.read(file); // Use UTF-8 by default (!useCharset isn't used!)
		validate(actualDescriptor1);	
		if (fileCharset == StandardCharsets.UTF_8) {
			assertEquals(EXPECTED_PROJECT_NAME, actualDescriptor1.getProject().getName().get());
			assertEquals(XPECTED_SCENARIO_NAME, actualDescriptor1.getProject().getScenarios().get(0).getName());
			assertEquals(XPECTED_POPULATION_NAME, actualDescriptor1.getProject().getScenarios().get(0).getPopulations().get(0).getName());
			
			assertEquals(expectedDescriptor.toString(), actualDescriptor1.toString());
		}
		else {
			assertNotEquals(EXPECTED_PROJECT_NAME, actualDescriptor1.getProject().getName().get());
			assertNotEquals(XPECTED_SCENARIO_NAME, actualDescriptor1.getProject().getScenarios().get(0).getName());
			assertNotEquals(XPECTED_POPULATION_NAME, actualDescriptor1.getProject().getScenarios().get(0).getPopulations().get(0).getName());
			
			assertNotEquals(expectedDescriptor.toString(), actualDescriptor1.toString());
		}		
		
		final IO mapper2 = new IO();
		final ProjectDescriptor actualDescriptor2 = mapper2.read(file, useCharset);
		validate(actualDescriptor2);	
		if (fileCharset == useCharset) {
			assertEquals(EXPECTED_PROJECT_NAME, actualDescriptor2.getProject().getName().get());
			assertEquals(XPECTED_SCENARIO_NAME, actualDescriptor2.getProject().getScenarios().get(0).getName());
			assertEquals(XPECTED_POPULATION_NAME, actualDescriptor2.getProject().getScenarios().get(0).getPopulations().get(0).getName());
			
			assertEquals(expectedDescriptor.toString(), actualDescriptor2.toString());
		}
		else {
			assertNotEquals(EXPECTED_PROJECT_NAME, actualDescriptor2.getProject().getName().get());
			assertNotEquals(XPECTED_SCENARIO_NAME, actualDescriptor2.getProject().getScenarios().get(0).getName());
			assertNotEquals(XPECTED_POPULATION_NAME, actualDescriptor2.getProject().getScenarios().get(0).getPopulations().get(0).getName());

			assertNotEquals(expectedDescriptor.toString(), actualDescriptor2.toString());
		}

		final IO mapper3 = new IO();
		final ProjectDescriptor actualDescriptor3 = mapper3.read(getContent(file, useCharset));
		validate(actualDescriptor3);	
		if (fileCharset == useCharset) {
			assertEquals(EXPECTED_PROJECT_NAME, actualDescriptor3.getProject().getName().get());
			assertEquals(XPECTED_SCENARIO_NAME, actualDescriptor3.getProject().getScenarios().get(0).getName());
			assertEquals(XPECTED_POPULATION_NAME, actualDescriptor3.getProject().getScenarios().get(0).getPopulations().get(0).getName());
			
			assertEquals(expectedDescriptor.toString(), actualDescriptor3.toString());
		}
		else {
			assertNotEquals(EXPECTED_PROJECT_NAME, actualDescriptor3.getProject().getName().get());
			assertNotEquals(XPECTED_SCENARIO_NAME, actualDescriptor3.getProject().getScenarios().get(0).getName());
			assertNotEquals(XPECTED_POPULATION_NAME, actualDescriptor3.getProject().getScenarios().get(0).getPopulations().get(0).getName());

			assertNotEquals(expectedDescriptor.toString(), actualDescriptor3.toString());
		}
		
		final IO mapper4 = new IO();
		final ProjectDescriptor actualDescriptor4 = mapper4.read(getContent(file, useCharset), ProjectDescriptor.class);
		validate(actualDescriptor4);	
		if (fileCharset == useCharset) {
			assertEquals(EXPECTED_PROJECT_NAME, actualDescriptor4.getProject().getName().get());
			assertEquals(XPECTED_SCENARIO_NAME, actualDescriptor4.getProject().getScenarios().get(0).getName());
			assertEquals(XPECTED_POPULATION_NAME, actualDescriptor4.getProject().getScenarios().get(0).getPopulations().get(0).getName());
			
			assertEquals(expectedDescriptor.toString(), actualDescriptor4.toString());
		}
		else {
			assertNotEquals(EXPECTED_PROJECT_NAME, actualDescriptor4.getProject().getName().get());
			assertNotEquals(XPECTED_SCENARIO_NAME, actualDescriptor4.getProject().getScenarios().get(0).getName());
			assertNotEquals(XPECTED_POPULATION_NAME, actualDescriptor4.getProject().getScenarios().get(0).getPopulations().get(0).getName());
 
			assertNotEquals(expectedDescriptor.toString(), actualDescriptor4.toString());
		}
	}

	@Test
	public void write() throws IOException {
		final ProjectDescriptor expectedDescriptor = getProjetDescriptorWithScenarios();
		assertNotNull(expectedDescriptor);
				
		writeAndCheck("test-scenarios-iso-8859-1", StandardCharsets.ISO_8859_1, expectedDescriptor, true);
		
		writeAndCheck("test-scenarios-utf-8", StandardCharsets.UTF_8, expectedDescriptor, true);	
	}

	private void writeAndCheck(final String fileName, final Charset fileCharset, final ProjectDescriptor expectedDescriptor, final boolean equals) throws IOException {
		assertNotNull(expectedDescriptor);
		
		writeAndCheck(fileName, "yaml", fileCharset, expectedDescriptor, equals);
		writeAndCheck(fileName, "json", fileCharset, expectedDescriptor, equals);
	}

	private void writeAndCheck(final String fileName, final String extension, final Charset fileCharset, final ProjectDescriptor expectedDescriptor, final boolean equals) throws IOException {
		final File file = getFile(fileName, extension);
		final String expectedContent = getContent(file, fileCharset).replace("\r\n", "\n");
		
		final IO writer = new IO();
		final String actualContent = writer.write(expectedDescriptor, Format.valueOf(extension.toUpperCase())); // Use UTF-8 by default
		assertTrue(actualContent.contains(EXPECTED_PROJECT_NAME));
		assertTrue(actualContent.contains(XPECTED_SCENARIO_NAME));
		assertTrue(actualContent.contains(XPECTED_POPULATION_NAME));		
		assertEquals(expectedContent, actualContent);
		
		final IO reader = new IO();
		final ProjectDescriptor actualDescriptor = reader.read(actualContent);	
		assertEquals(EXPECTED_PROJECT_NAME, actualDescriptor.getProject().getName().get());
		assertEquals(XPECTED_SCENARIO_NAME, actualDescriptor.getProject().getScenarios().get(0).getName());
		assertEquals(XPECTED_POPULATION_NAME, actualDescriptor.getProject().getScenarios().get(0).getPopulations().get(0).getName());		
		assertEquals(expectedDescriptor.toString(), actualDescriptor.toString());
	}
}
