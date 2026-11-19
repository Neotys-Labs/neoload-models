package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.variable.RandomStringVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

/**
 * The lengths of a random_string variable read from YAML go up to 1000 characters; min_length may be 0 for an
 * empty value.
 */
public class IORandomStringLengthTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String VARIABLE = "project.variables[0]";

	static String randomString(final String minLength, final String maxLength) {
		return "name: P\nvariables:\n- random_string:\n    name: v\n    min_length: " + minLength + "\n    max_length: " + maxLength + "\n    predictable: false\n";
	}

	private static Validation validate(final String yaml) throws IOException {
		return VALIDATOR.validate(new IO().read(yaml), NeoLoad.class);
	}

	@Test
	public void minLengthAboveMaxIsRejected() throws IOException {
		assertViolations(validate(randomString("2000", "1000")),
				invalidValue("2000", VARIABLE + ".min_length", "must be between 0 and 1000."),
				invalidValue(VARIABLE, "random_string 'min_length' must be less than or equal to 'max_length'."));
	}

	@Test
	public void maxLengthAboveMaxIsRejected() throws IOException {
		assertViolations(validate(randomString("1", "2000")), invalidValue("2000", VARIABLE + ".max_length", "must be between 1 and 1000."));
	}

	@Test
	public void zeroMinLengthIsAccepted() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(randomString("0", "10"));

		assertTrue(VALIDATOR.validate(descriptor, NeoLoad.class).isValid());
		assertEquals(0, ((RandomStringVariable) descriptor.getProject().getVariables().get(0)).getMinLength());
	}
}
