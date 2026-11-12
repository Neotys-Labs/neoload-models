package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.Test;

public class IOUniqueNamesTest {

	private static Validation validate(final String fixture) throws IOException, URISyntaxException {
		final String fileName = "test-unique-names-" + fixture + ".yaml";
		final URL url = IOUniqueNamesTest.class.getClassLoader().getResource(fileName);
		assertNotNull("Missing classpath resource " + fileName, url);
		final String yaml = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(url).toURI())), StandardCharsets.UTF_8);
		return new Validator().validate(new IO().read(yaml, Project.class), NeoLoad.class);
	}

	private static String message(final Validation validation) {
		return validation.getMessage().orElse("");
	}

	private static void assertDuplicate(final String fixture, final String path, final String duplicates) throws IOException, URISyntaxException {
		final Validation validation = validate(fixture);
		assertFalse(validation.isValid());
		assertTrue(message(validation), message(validation).contains("Incorrect value for '" + path + "': must contain only unique names. Duplicates: " + duplicates + "."));
	}

	private static void assertNoUniquenessError(final String fixture) throws IOException, URISyntaxException {
		final Validation validation = validate(fixture);
		assertFalse(message(validation), message(validation).contains("must contain only unique"));
	}

	@Test
	public void duplicatesAreReportedDistinctInOrderOfFirstAppearance() throws IOException, URISyntaxException {
		assertDuplicate("distinct-in-order-of-first-appearance", "variables", "foo, bar");
	}

	@Test
	public void everyCheckedListRejectsDuplicateNames() throws IOException, URISyntaxException {
		assertDuplicate("duplicate-servers", "servers", "dup");
		assertDuplicate("duplicate-user-paths", "user_paths", "dup");
		assertDuplicate("duplicate-populations", "populations", "dup");
		assertDuplicate("duplicate-sla-profiles", "sla_profiles", "dup");
		assertDuplicate("duplicate-scenarios", "scenarios", "dup");
		assertDuplicate("duplicate-scenario-populations", "scenarios[0].populations", "dup");
		assertDuplicate("duplicate-shared-elements", "shared_elements", "dup");
		assertDuplicate("duplicate-content-assertions", "user_paths[0].actions.steps[0].content_assertions", "dup");
		assertDuplicate("duplicate-request-profiles", "web_services_security.request_profiles", "dup");
		assertDuplicate("duplicate-response-profiles", "web_services_security.response_profiles", "dup");
	}

	@Test
	public void projectWithoutDuplicatesIsAccepted() throws IOException, URISyntaxException {
		final Validation validation = validate("no-duplicates");
		assertTrue(message(validation), validation.isValid());
	}

	@Test
	public void nameComparisonIsCaseSensitive() throws IOException, URISyntaxException {
		assertNoUniquenessError("case-sensitive");
	}

	@Test
	public void duplicateKeystorePathsAreReportedAsPaths() throws IOException, URISyntaxException {
		final Validation validation = validate("duplicate-keystore-paths");
		assertTrue(message(validation), message(validation).contains("Incorrect value for 'web_services_security.keystores': must contain only unique paths. Duplicates: ./wss-keystores/a.p12."));
	}

	@Test
	public void duplicateRendezvousPoliciesInOneScenarioAreRejected() throws IOException, URISyntaxException {
		assertDuplicate("duplicate-rendezvous-policies", "scenarios[0].rendezvous_policies", "rdv");
	}

	@Test
	public void sameRendezvousPolicyNameInTwoScenariosIsAllowed() throws IOException, URISyntaxException {
		assertNoUniquenessError("rendezvous-policy-in-two-scenarios");
	}

	@Test
	public void contentAssertionsWithoutNameNeverCountAsDuplicates() throws IOException, URISyntaxException {
		final Validation validation = validate("content-assertions-without-name");
		assertFalse(validation.isValid());
		final String message = message(validation);
		assertTrue(message, message.contains("Duplicates: check."));
		assertTrue(message, message.indexOf("must contain only unique") == message.lastIndexOf("must contain only unique"));
	}

	@Test
	public void emptyListItemDoesNotCrashUniquenessValidation() throws IOException, URISyntaxException {
		assertNoUniquenessError("empty-list-item");
	}

	@Test
	public void listsLeftAsIsStillAcceptDuplicates() throws IOException, URISyntaxException {
		assertNoUniquenessError("lists-left-as-is");
	}

	@Test
	public void messageParametersAreNotInterpolated() throws IOException, URISyntaxException {
		assertDuplicate("uninterpolated-message-parameters", "servers", "${1+1}{x}");
	}
}
