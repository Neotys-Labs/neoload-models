package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.variable.RandomStringVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.time.Duration;
import org.junit.Test;

/**
 * A value above what NeoLoad supports is reported, never replaced by the closest supported value.
 */
public class IOOutOfRangeValueTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String PROJECT = "name: P\n"
			+ "servers:\n- name: S\n  host: myserver.intranet.neotys.com\n  port: 70000\n"
			+ "variables:\n- random_string:\n    name: v\n    min_length: 1\n    max_length: 2000\n    predictable: false\n"
			+ "scenarios:\n- name: S\n  populations:\n  - name: C\n    constant_load:\n      users: 1\n      rampup: 2147484\n";

	@Test
	public void rejectedValuesAreReportedAndKept() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(PROJECT);
		final Project project = descriptor.getProject();

		assertViolations(VALIDATOR.validate(descriptor, NeoLoad.class),
				invalidValue("2000", "project.variables[0].max_length", "must be between 1 and 1000."),
				invalidValue("596h31m24s", "project.scenarios[0].populations[0].constant_load.rampup", "must be between 1 and 2147483 seconds."),
				invalidValue("project.servers[0].port", "must be a number between 1 and 65535, got 70000."));
		assertEquals(Duration.ofSeconds(2147484), ((ConstantLoadPolicy) project.getScenarios().get(0).getPopulations().get(0).getLoadPolicy()).getRampup());
		assertEquals("70000", project.getServers().get(0).getPort());
		assertEquals(2000, ((RandomStringVariable) project.getVariables().get(0)).getMaxLength());
	}
}
