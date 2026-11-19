package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.MISSING_VALUE;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The minimum users of a rampup and of a peaks minimum phase can be left out, meaning 0 users: an omitted value is
 * accepted by schema 3.2 and the model, and is not written back. The users of a peaks maximum phase stay mandatory.
 */
public class IOMinimumUsersTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String RAMPUP_LOAD = "project.scenarios[0].populations[0].rampup_load";
	private static final String PEAKS_LOAD = "project.scenarios[0].populations[0].peaks_load";

	private static final String MINIMUM_USERS_OMITTED = DurationFields.scenario("  populations:\n"
			+ "  - name: R\n    rampup_load:\n      max_users: 5\n      increment_users: 1\n      increment_every: 1 iteration\n"
			+ "  - name: K\n    peaks_load:\n      minimum:\n        duration: 1 iteration\n      maximum:\n        users: 5\n        duration: 1 iteration\n"
			+ "      start: maximum\n");

	private static SchemaFiles schema30;
	private static SchemaFiles schema31;
	private static SchemaFiles schema32;

	@BeforeClass
	public static void loadSchemas() throws URISyntaxException {
		schema30 = SchemaFiles.version("v3.0");
		schema31 = SchemaFiles.version("v3.1");
		schema32 = SchemaFiles.version("v3.2");
	}

	private static String rampup(final String minUsers, final String maxUsers) {
		return DurationFields.scenario("  populations:\n  - name: R\n    rampup_load:\n" + minUsers + maxUsers
				+ "      increment_users: 1\n      increment_every: 1 iteration\n");
	}

	private static String peaksMinimumUsers(final String users) {
		return DurationFields.scenario("  populations:\n  - name: K\n    peaks_load:\n      minimum:\n        users: " + users + "\n        duration: 1 iteration\n"
				+ "      maximum:\n        users: 5\n        duration: 1 iteration\n      start: maximum\n");
	}

	private static Validation validate(final ProjectDescriptor descriptor) {
		return VALIDATOR.validate(descriptor, NeoLoad.class);
	}

	private static LoadPolicy loadPolicy(final ProjectDescriptor descriptor, final int population) {
		return descriptor.getProject().getScenarios().get(0).getPopulations().get(population).getLoadPolicy();
	}

	@Test
	public void omittedMinimumUsersAreAccepted() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(MINIMUM_USERS_OMITTED);
		final Validation validation = validate(descriptor);

		assertTrue(validation.getMessage().orElse(""), validation.isValid());
		assertFalse(((RampupLoadPolicy) loadPolicy(descriptor, 0)).getMinUsers().isPresent());
		assertFalse(((PeaksLoadPolicy) loadPolicy(descriptor, 1)).getMinimum().getUsers().isPresent());
		assertTrue(schema32.accepts(MINIMUM_USERS_OMITTED));
	}

	@Test
	public void omittedMinimumUsersAreNotWrittenBack() throws IOException {
		final IO io = new IO();

		final String written = io.write(io.read(MINIMUM_USERS_OMITTED), Format.YAML);

		assertFalse(written, written.contains("min_users"));
		assertFalse(written, written.contains("minimum:\n        users"));
		assertTrue(written, written.contains("maximum:\n        users: 5"));
		assertTrue(written, written.contains("max_users: 5"));
		assertTrue(written, schema32.accepts(written));
	}

	@Test
	public void outOfRangeMinimumUsersViolationNamesTheFieldAndTheValue() throws IOException {
		assertViolations(validate(new IO().read(rampup("      min_users: 0\n", ""))),
				invalidValue("0", RAMPUP_LOAD + ".min_users", "must be greater than or equal to 1 user."));
		assertViolations(validate(new IO().read(rampup("      min_users: -1\n", ""))),
				invalidValue("-1", RAMPUP_LOAD + ".min_users", "must be greater than or equal to 1 user."));
		assertViolations(validate(new IO().read(peaksMinimumUsers("0"))),
				invalidValue("0", PEAKS_LOAD + ".minimum.users", "must be greater than or equal to 1 user."));
		assertViolations(validate(new IO().read(peaksMinimumUsers("-1"))),
				invalidValue("-1", PEAKS_LOAD + ".minimum.users", "must be greater than or equal to 1 user."));
	}

	@Test
	public void peaksMaximumWithoutUsersIsRejected() throws IOException {
		final String yaml = DurationFields.scenario("  populations:\n  - name: K\n    peaks_load:\n      minimum:\n        users: 1\n        duration: 1 iteration\n"
				+ "      maximum:\n        duration: 1 iteration\n      start: maximum\n");

		assertViolations(validate(new IO().read(yaml)), invalidValue(PEAKS_LOAD + ".maximum", MISSING_VALUE));
		assertFalse(schema32.accepts(yaml));
	}

	@Test
	public void rampupWithoutMaxUsersIsAccepted() throws IOException {
		final String yaml = rampup("      min_users: 1\n", "");
		final Validation validation = validate(new IO().read(yaml));

		assertTrue(validation.getMessage().orElse(""), validation.isValid());
		assertTrue(schema32.accepts(yaml));
	}

	@Test
	public void minUsersGreaterThanMaxUsersIsRejected() throws IOException {
		assertViolations(validate(new IO().read(rampup("      min_users: 6\n", "      max_users: 5\n"))),
				invalidValue(RAMPUP_LOAD, "rampup_load 'min_users' must be less than or equal to 'max_users'."));
	}

	@Test
	public void minUsersEqualToMaxUsersOrOmittedIsAccepted() throws IOException {
		final Validation equal = validate(new IO().read(rampup("      min_users: 5\n", "      max_users: 5\n")));
		final Validation omitted = validate(new IO().read(rampup("", "      max_users: 5\n")));

		assertTrue(equal.getMessage().orElse(""), equal.isValid());
		assertTrue(omitted.getMessage().orElse(""), omitted.isValid());
	}

	@Test
	public void olderSchemasKeepRequiringTheMinimumUsers() throws IOException {
		assertFalse(schema30.accepts(rampup("", "      max_users: 5\n")));
		assertFalse(schema31.accepts(rampup("", "      max_users: 5\n")));
		assertTrue(schema31.accepts(rampup("      min_users: 1\n", "      max_users: 5\n")));
	}
}
