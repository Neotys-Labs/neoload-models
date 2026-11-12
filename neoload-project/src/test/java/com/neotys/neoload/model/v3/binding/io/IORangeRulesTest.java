package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertInvalidDurationRead;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
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

	private static final String START_AFTER = DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      start_after: %s\n");

	private static SchemaFiles schema31;

	@BeforeClass
	public static void loadSchemas() throws URISyntaxException {
		schema31 = SchemaFiles.version("v3.1");
	}

	private static Project read(final String yaml) throws IOException {
		return new IO().read(yaml).getProject();
	}

	private static LoadPolicy loadPolicy(final Project project, final int population) {
		return project.getScenarios().get(0).getPopulations().get(population).getLoadPolicy();
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

}
