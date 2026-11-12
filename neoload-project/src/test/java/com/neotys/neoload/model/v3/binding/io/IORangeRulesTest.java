package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertInvalidDurationRead;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The behaviors around the literal range rules of the v3.1 as-code fields that the boundary parity test does not
 * show: omitted minimum users, a {@code start_after} that is not a population name, the reported errors and the
 * unchanged 3.0 schema.
 */
public class IORangeRulesTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String MINIMUM_USERS_OMITTED = DurationFields.scenario("  populations:\n"
			+ "  - name: R\n    rampup_load:\n      max_users: 5\n      increment_users: 1\n      increment_every: 1 iteration\n      duration: 15 iterations\n"
			+ "  - name: K\n    peaks_load:\n      minimum:\n        duration: 1 iteration\n      maximum:\n        users: 5\n        duration: 1 iteration\n"
			+ "      start: maximum\n      duration: 15 iterations\n");

	private static final String START_AFTER = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: %s\n");

	private static final String PORT_AND_LOOP_COUNT = "name: P\nservers:\n- name: S\n  host: myserver.intranet.neotys.com\n  port: 70000\n"
			+ "user_paths:\n- name: U\n  actions:\n    steps:\n    - loop:\n        count: 0\n        steps:\n        - delay: 1s\n";

	private static SchemaFiles schema31;
	private static SchemaFiles schema30;

	@BeforeClass
	public static void loadSchemas() throws URISyntaxException {
		schema31 = SchemaFiles.version("v3.1");
		schema30 = SchemaFiles.version("v3.0");
	}

	private static Project read(final String yaml) throws IOException {
		return new IO().read(yaml).getProject();
	}

	private static Validation validate(final Project project) {
		return VALIDATOR.validate(ProjectDescriptor.builder().project(project).build(), NeoLoad.class);
	}

	private static LoadPolicy loadPolicy(final Project project, final int population) {
		return project.getScenarios().get(0).getPopulations().get(population).getLoadPolicy();
	}

	@Test
	public void minimumUsersOmittedMeansNoMinimumUsers() throws IOException {
		assertTrue(schema31.accepts(MINIMUM_USERS_OMITTED));
		final Project project = read(MINIMUM_USERS_OMITTED);

		assertTrue(validate(project).getMessage().orElse(""), validate(project).isValid());
		assertFalse(((RampupLoadPolicy) loadPolicy(project, 0)).getMinUsers().isPresent());
		assertFalse(((PeaksLoadPolicy) loadPolicy(project, 1)).getMinimum().getUsers().isPresent());
	}

	@Test
	public void omittedMinimumUsersAreNotWrittenBack() throws IOException {
		final Project project = read(MINIMUM_USERS_OMITTED);

		final String written = new IO().write(ProjectDescriptor.builder().project(project).build(), Format.YAML);

		assertFalse(written, written.contains("min_users"));
		assertFalse(written, written.contains("minimum:\n        users"));
		assertTrue(written, written.contains("users: 5"));
		assertTrue(written, schema31.accepts(written));
	}

	@Test
	public void startAfterWithAnInvalidDurationFailsTheReadAndIsRejectedBySchema() throws IOException {
		for (final String value : new String[] {"-5s", "-5", "5x", "1s1h"}) {
			final String yaml = String.format(START_AFTER, value);

			assertInvalidDurationRead(() -> read(yaml), value, "start_after");
			assertFalse(value, schema31.accepts(yaml));
		}
	}

	@Test
	public void startAfterNamingAPopulationIsReadAsAPopulation() throws IOException {
		final String yaml = String.format(START_AFTER, "other_population");

		final ConstantLoadPolicy policy = (ConstantLoadPolicy) loadPolicy(read(yaml), 0);

		assertEquals(StartAfter.builder().type(StartAfter.Type.POPULATION).value("other_population").build(), policy.getStartAfter());
		assertTrue(schema31.accepts(yaml));
	}

	@Test
	public void outOfRangeLiteralIsReportedWithItsFieldAndValueAndKept() throws IOException {
		final Project project = read(PORT_AND_LOOP_COUNT);

		assertViolations(validate(project),
				invalidValue("project.servers[0].port", "must be a number between 1 and 65535, got 70000."),
				invalidValue("project.user_paths[0].actions.steps[0].count", "must be a number between 1 and 2147483647, got 0."));
		assertEquals("70000", project.getServers().get(0).getPort());
	}

	@Test
	public void readStopsAtTheFirstInvalidDuration() {
		final String yaml = DurationFields.scenario("  populations:\n"
				+ "  - name: A\n    constant_load:\n      users: 1\n      rampup: -5s\n"
				+ "  - name: B\n    constant_load:\n      users: 1\n      rampup: abc\n");

		assertInvalidDurationRead(() -> read(yaml), "-5s", "rampup");
	}

	@Test
	public void schema30KeepsItsAcceptance() throws IOException {
		final String yaml = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 0\n")
				+ "servers:\n- name: S\n  host: myserver.intranet.neotys.com\n  port: 70000\n";

		assertTrue(schema30.accepts(yaml));
		assertFalse(schema31.accepts(yaml));
	}
}
