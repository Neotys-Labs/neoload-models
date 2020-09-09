package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;


abstract class AbstractIOElementsTest {

	private static final Validator VALIDATOR = new Validator();

	protected void read(final String fileName, final Project expectedProject) throws IOException {
		assertNotNull(expectedProject);
		
		read(fileName, ProjectDescriptor.builder().project(expectedProject).build());
	}

	protected void read(final String fileName, final ProjectDescriptor expectedDescriptor) throws IOException {
		assertNotNull(expectedDescriptor);
		
		read(fileName, "yaml", expectedDescriptor);
		read(fileName, "json", expectedDescriptor);
	}
	
	private void read(final String fileName, final String extension, final ProjectDescriptor expectedDescriptor) throws IOException {
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(Objects.requireNonNull(classLoader.getResource(fileName + "." + extension)).getFile());
		
		final IO mapper1 = new IO();
		final ProjectDescriptor actualDescriptor1 = mapper1.read(file);
		validate(actualDescriptor1);	
		assertEquals(expectedDescriptor.toString(), actualDescriptor1.toString());
		
		final IO mapper2 = new IO();
		final ProjectDescriptor actualDescriptor2 = mapper2.read(new String(Files.readAllBytes(Paths.get(file.toURI()))));
		validate(actualDescriptor2);	
		assertEquals(expectedDescriptor.toString(), actualDescriptor2.toString());
	}

	protected void write(final String fileName, final Project expectedProject) throws IOException {
		assertNotNull(expectedProject);
		
		write(fileName, ProjectDescriptor.builder().project(expectedProject).build());
	}

	protected void write(final String fileName, final ProjectDescriptor expectedDescriptor) throws IOException {
		assertNotNull(expectedDescriptor);
		
		write(fileName, "yaml", expectedDescriptor);
		write(fileName, "json", expectedDescriptor);
	}

	private void write(final String fileName, final String extension, final ProjectDescriptor expectedDescriptor) throws IOException {
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(Objects.requireNonNull(classLoader.getResource(fileName + "." + extension)).getFile());
		
		final String expectedContent = new String(Files.readAllBytes(Paths.get(file.toURI()))).replace("\r\n", "\n");
		
		final IO mapper = new IO();
		final String actualContent = mapper.write(expectedDescriptor, Format.valueOf(extension.toUpperCase()));
		assertEquals(expectedContent, actualContent);	
	}

	private void validate(final ProjectDescriptor descriptor) {
		final Validation validation = VALIDATOR.validate(descriptor, NeoLoad.class);
		if (!validation.isValid()) {
			fail(validation.getMessage().get());
		}
	}
}
