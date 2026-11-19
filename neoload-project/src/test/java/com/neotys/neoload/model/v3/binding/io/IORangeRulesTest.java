package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertInvalidDurationRead;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The behaviors around the literal range rules of the v3.2 as-code fields that the boundary parity test does not
 * show: a {@code start_after} that is a delay, a population name or neither, read with {@link IO} and checked
 * against schema 3.2 side by side.
 */
public class IORangeRulesTest {

	private static final String START_AFTER = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: %s\n");

	private static SchemaFiles schema30;
	private static SchemaFiles schema31;
	private static SchemaFiles schema32;

	@BeforeClass
	public static void loadSchemas() throws URISyntaxException {
		schema30 = SchemaFiles.version("v3.0");
		schema31 = SchemaFiles.version("v3.1");
		schema32 = SchemaFiles.version("v3.2");
	}

	private static StartAfter startAfter(final String yaml) throws IOException {
		final Project project = new IO().read(yaml).getProject();
		return ((ConstantLoadPolicy) project.getScenarios().get(0).getPopulations().get(0).getLoadPolicy()).getStartAfter();
	}

	@Test
	public void startAfterNeitherADurationNorAPopulationNameFailsTheReadAndIsRejectedBySchema() throws IOException {
		for (final String literal : new String[] {"pop#1", "pop|1"}) {
			final String yaml = String.format(START_AFTER, literal);

			assertInvalidDurationRead(() -> startAfter(yaml), literal, "start_after");
			assertFalse(literal, schema32.accepts(yaml));
		}
	}

	@Test
	public void startAfterNegativeNumberFailsTheRead() {
		final String yaml = String.format(START_AFTER, "\"-5\"");

		assertInvalidDurationRead(() -> startAfter(yaml), "-5", "start_after");
	}

	@Test
	public void startAfterOverflowingALongFailsTheRead() {
		final String yaml = String.format(START_AFTER, "99999999999999999999");

		assertInvalidDurationRead(() -> startAfter(yaml), "99999999999999999999", "start_after");
	}

	@Test
	public void startAfterNamingAPopulationIsReadAsAPopulation() throws IOException {
		for (final String value : new String[] {"other_population", "5x", "-5s", "1s1h"}) {
			final String yaml = String.format(START_AFTER, value);

			assertEquals(StartAfter.builder().type(StartAfter.Type.POPULATION).value(value).build(), startAfter(yaml));
			assertTrue(value, schema32.accepts(yaml));
		}
	}

	@Test
	public void startAfterHoldingADurationIsReadAsADelayInSeconds() throws IOException {
		for (final Object[] delay : new Object[][] {{"5s", 5L}, {"10", 10L}, {"1m", 60L}}) {
			final String yaml = String.format(START_AFTER, delay[0]);

			assertEquals(StartAfter.builder().type(StartAfter.Type.TIME).value(Duration.ofSeconds((Long) delay[1])).build(), startAfter(yaml));
			assertTrue((String) delay[0], schema32.accepts(yaml));
		}
	}

	@Test
	public void startAfterHoldingAVariableReferenceStaysAccepted() throws IOException {
		final String yaml = String.format(START_AFTER, "\"${pop}\"");
		final ProjectDescriptor descriptor = new IO().read(yaml);

		assertTrue(new Validator().validate(descriptor, NeoLoad.class).isValid());
		assertEquals(StartAfter.builder().type(StartAfter.Type.POPULATION).value("${pop}").build(), startAfter(yaml));
		assertTrue(schema32.accepts(yaml));
	}

	@Test
	public void olderSchemasKeepAcceptingAnyStartAfterText() throws IOException {
		final String yaml = String.format(START_AFTER, "pop#1");

		assertTrue(schema30.accepts(yaml));
		assertTrue(schema31.accepts(yaml));
	}
}
