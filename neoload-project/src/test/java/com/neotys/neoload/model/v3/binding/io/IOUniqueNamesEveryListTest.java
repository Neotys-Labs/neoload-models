package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.validation.validator.Validation;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IOUniqueNamesEveryListTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> lists() {
		return Arrays.asList(new Object[][] {
				{ "duplicate-servers", "servers" },
				{ "duplicate-user-paths", "user_paths" },
				{ "duplicate-populations", "populations" },
				{ "duplicate-sla-profiles", "sla_profiles" },
				{ "duplicate-scenarios", "scenarios" },
				{ "duplicate-scenario-populations", "scenarios[0].populations" },
				{ "duplicate-shared-elements", "shared_elements" },
				{ "duplicate-content-assertions", "user_paths[0].actions.steps[0].content_assertions" },
				{ "duplicate-request-profiles", "web_services_security.request_profiles" },
				{ "duplicate-response-profiles", "web_services_security.response_profiles" }
		});
	}

	private final String fixture;
	private final String path;

	public IOUniqueNamesEveryListTest(final String fixture, final String path) {
		this.fixture = fixture;
		this.path = path;
	}

	@Test
	public void listRejectsDuplicateNames() throws IOException, URISyntaxException {
		final Validation validation = IOUniqueNamesTest.validate(fixture);
		final String message = IOUniqueNamesTest.message(validation);
		assertFalse(validation.isValid());
		assertTrue(message, message.contains(IOUniqueNamesTest.duplicateNamesMessage(path, "dup")));
	}
}
