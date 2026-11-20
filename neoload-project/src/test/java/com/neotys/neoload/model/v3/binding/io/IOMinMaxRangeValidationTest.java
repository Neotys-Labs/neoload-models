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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.Test;

public class IOMinMaxRangeValidationTest {

	private static final List<String> INVERTED_RANGE_VIOLATIONS = Arrays.asList(
			"Incorrect value for 'project.variables[0]': random_number 'min' must be less than or equal to 'max'.",
			"Incorrect value for 'project.variables[1]': random_string 'min_length' must be less than or equal to 'max_length'.",
			"Incorrect value for 'project.user_paths[0].actions.pacing': duration 'min' must be less than or equal to 'max'.",
			"Incorrect value for 'project.user_paths[0].actions.steps[0].delay': duration 'min' must be less than or equal to 'max'.",
			"Incorrect value for 'project.user_paths[0].actions.steps[1].think_time': duration 'min' must be less than or equal to 'max'.",
			"Incorrect value for 'project.user_paths[0].actions.steps[2].think_time': duration 'min' must be less than or equal to 'max'.",
			"Incorrect value for 'project.scenarios[0].populations[0].rampup_load': rampup_load 'min_users' must be less than or equal to 'max_users'.");

	private static File getFile(final String fileName) throws URISyntaxException {
		final URL url = IOMinMaxRangeValidationTest.class.getClassLoader().getResource(fileName);
		assertNotNull("Missing classpath resource " + fileName, url);
		return new File(Objects.requireNonNull(url).toURI());
	}

	@Test
	public void everyRangeWithMinGreaterThanMaxIsRejected() throws IOException, URISyntaxException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-min-max-ranges-inverted.yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertFalse(validation.isValid());

		final String message = validation.getMessage().get();
		assertTrue(message, message.contains("Violation Number: " + INVERTED_RANGE_VIOLATIONS.size() + "."));
		INVERTED_RANGE_VIOLATIONS.forEach(violation -> assertTrue(message, message.contains(violation)));
	}

	@Test
	public void rangesWithEqualBoundsOrVariableBoundsAreAccepted() throws IOException, URISyntaxException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-min-max-ranges-valid.yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
		assertTrue(validation.getMessage().orElse(""), validation.isValid());
	}
}
