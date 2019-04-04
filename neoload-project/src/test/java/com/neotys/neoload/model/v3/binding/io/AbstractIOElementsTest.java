package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


abstract class AbstractIOElementsTest {

	private static final Validator VALIDATOR = new Validator();

	protected void read(final String fileName, final Project expectedProject) throws IOException {
		assertNotNull(expectedProject);
		
		read(fileName, "yaml", ProjectDescriptor.builder().project(expectedProject).build());
		read(fileName, "json", ProjectDescriptor.builder().project(expectedProject).build());
	}

	protected void read(final String fileName, final ProjectDescriptor expectedDescriptor) throws IOException {
		assertNotNull(expectedDescriptor);
		
		read(fileName, "yaml", expectedDescriptor);
		read(fileName, "json", expectedDescriptor);
	}
	
	protected void read(final String fileName, final String extension, final ProjectDescriptor expectedDescriptor) throws IOException {
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(Objects.requireNonNull(classLoader.getResource(fileName + "." + extension)).getFile());
		
		final IO mapper1 = new IO();
		final ProjectDescriptor actualDescriptor1 = mapper1.read(file);
		validate(actualDescriptor1);	
		assertEquals(expectedDescriptor, actualDescriptor1);
		
		final IO mapper2 = new IO();
		final ProjectDescriptor actualDescriptor2 = mapper2.read(new String(Files.readAllBytes(Paths.get(file.toURI()))));
		validate(actualDescriptor2);	
		assertEquals(expectedDescriptor, actualDescriptor2);
	}

//	protected void read(final String fileName, final String extension, final Project expectedProject) throws IOException {
//		final ClassLoader classLoader = getClass().getClassLoader();
//		final File file = new File(Objects.requireNonNull(classLoader.getResource(fileName + "." + extension)).getFile());
//		
//		final IO mapper1 = new IO();
//		final Project actualProject1 = mapper1.read(file, Project.class);
//		validate(actualProject1);
//		assertEquals(expectedProject, actualProject1);
//		
//		final IO mapper2 = new IO();
//		final Project actualProject2 = mapper2.read(new String(Files.readAllBytes(Paths.get(file.toURI()))), Project.class);
//		validate(actualProject2);
//		assertEquals(expectedProject, actualProject2);
//	}

	private void validate(final ProjectDescriptor descriptor) {
		final Validation validation = VALIDATOR.validate(descriptor, NeoLoad.class);
		if (!validation.isValid()) {
			fail(validation.getMessage().get());
		}
	}
	
//	protected void write(final String fileName, final Project expectedProject) throws IOException {
//		assertNotNull(expectedProject);
//		
//		write(fileName, "yaml", expectedProject);
//		write(fileName, "json", expectedProject);
//	}
//
//	protected void write(final String fileName, final String extension, final Project expectedProject) throws IOException {
//		final ClassLoader classLoader = getClass().getClassLoader();
//		final File file = new File(Objects.requireNonNull(classLoader.getResource(fileName + "." + extension)).getFile());
//		
//		final IO mapper = new IO();
//
//		String expectedContent = new String(Files.readAllBytes(Paths.get(file.toURI()))); 
//		expectedContent = expectedContent.trim();
//		expectedContent = expectedContent.replace("\r\n", "\n");
//		
//		final File actualFile = File.createTempFile(fileName + "-", "." + extension);
//		mapper.write(actualFile, expectedProject);
//		String actualContent = new String(Files.readAllBytes(Paths.get(actualFile.toURI())));
//		assertNotNull(actualContent);
//		actualContent = actualContent.trim();
//		actualContent = actualContent.replace("\r\n", "\n");
//		assertEquals(expectedContent, actualContent);			
//		
//		final Project actualProject = mapper.read(actualFile, Project.class);
//		assertNotNull(actualProject);
//		assertEquals(expectedProject, actualProject);		
//	}	
}
