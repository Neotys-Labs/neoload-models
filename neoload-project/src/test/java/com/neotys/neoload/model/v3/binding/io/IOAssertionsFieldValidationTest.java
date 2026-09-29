package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import org.junit.Test;

public class IOAssertionsFieldValidationTest {

	private static File getFile(final String fileName) throws URISyntaxException {
		final URL url = IOAssertionsFieldValidationTest.class.getClassLoader().getResource(fileName);
		assertNotNull("Missing classpath resource " + fileName, url);
		return new File(Objects.requireNonNull(url).toURI());
	}

	@Test
	public void bothAssertionsAndContentAssertionsFilledIsRejected() throws IOException, URISyntaxException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-assertions-mutually-exclusive.yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());

		final String message = validation.getMessage().get();
		assertTrue(message, message.contains("user_paths[0].actions.steps[0].content_assertions"));
		assertTrue(message, message.contains("invalid attributes usage (assertions and content_assertions cannot be used simultaneously)."));
	}

	@Test
	public void legacyAssertionsKeyReportsViolationUnderItsOwnFieldName() throws IOException, URISyntaxException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-legacy-assertions-duplicate-names.yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());

		final String message = validation.getMessage().get();
		assertTrue(message, message.contains("user_paths[0].actions.steps[0].assertions': must contain only unique names."));
		assertFalse(message, message.contains("content_assertions"));
	}
}
