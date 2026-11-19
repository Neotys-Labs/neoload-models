package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

/**
 * The port of a server read from YAML is a number from 1 to 65535 or a variable.
 */
public class IOServerPortTest {
	private static final Validator VALIDATOR = new Validator();

	private static final String PORT = "project.servers[0].port";

	static String server(final String port) {
		return "name: P\nservers:\n- name: S\n  host: myserver.intranet.neotys.com\n  port: " + port + "\n";
	}

	private static Validation validate(final String yaml) throws IOException {
		return VALIDATOR.validate(new IO().read(yaml), NeoLoad.class);
	}

	@Test
	public void zeroPortIsRejected() throws IOException {
		assertViolations(validate(server("0")), invalidValue(PORT, "must be a number between 1 and 65535, got 0."));
	}

	@Test
	public void portAboveMaxIsRejected() throws IOException {
		assertViolations(validate(server("65536")), invalidValue(PORT, "must be a number between 1 and 65535, got 65536."));
	}

	@Test
	public void portFarAboveMaxIsRejected() throws IOException {
		assertViolations(validate(server("70000")), invalidValue(PORT, "must be a number between 1 and 65535, got 70000."));
	}

	@Test
	public void variablePortIsAccepted() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(server("\"${port}\""));

		assertTrue(VALIDATOR.validate(descriptor, NeoLoad.class).isValid());
		assertEquals("${port}", descriptor.getProject().getServers().get(0).getPort());
	}

	@Test
	public void numberAsStringPortIsAcceptedByTheModel() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(server("\"8080\""));

		assertTrue(VALIDATOR.validate(descriptor, NeoLoad.class).isValid());
		assertEquals("8080", descriptor.getProject().getServers().get(0).getPort());
	}
}
