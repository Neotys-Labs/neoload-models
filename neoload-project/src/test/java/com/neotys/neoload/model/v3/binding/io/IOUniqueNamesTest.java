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

	private static final String NOT_UNIQUE = "must contain only unique";

	static Validation validate(final String fixture) throws IOException, URISyntaxException {
		final String fileName = "test-unique-names-" + fixture + ".yaml";
		final URL url = IOUniqueNamesTest.class.getClassLoader().getResource(fileName);
		assertNotNull("Missing classpath resource " + fileName, url);
		final String yaml = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(url).toURI())), StandardCharsets.UTF_8);
		return new Validator().validate(new IO().read(yaml, Project.class), NeoLoad.class);
	}

	static String message(final Validation validation) {
		return validation.getMessage().orElse("");
	}

	static String duplicateNamesMessage(final String path, final String duplicates) {
		return "Incorrect value for '" + path + "': must contain only unique names. Duplicates: " + duplicates + ".";
	}

	@Test
	public void duplicatesAreReportedDistinctInOrderOfFirstAppearance() throws IOException, URISyntaxException {
		final Validation validation = validate("distinct-in-order-of-first-appearance");
		final String message = message(validation);
		assertFalse(validation.isValid());
		assertTrue(message, message.contains(duplicateNamesMessage("variables", "foo, bar")));
	}

	@Test
	public void projectWithoutDuplicatesIsAccepted() throws IOException, URISyntaxException {
		final Validation validation = validate("no-duplicates");
		assertTrue(message(validation), validation.isValid());
	}

	@Test
	public void nameComparisonIsCaseSensitive() throws IOException, URISyntaxException {
		final String message = message(validate("case-sensitive"));
		assertFalse(message, message.contains(NOT_UNIQUE));
	}

	@Test
	public void duplicateKeystorePathsAreReportedAsPaths() throws IOException, URISyntaxException {
		final Validation validation = validate("duplicate-keystore-paths");
		assertTrue(message(validation), message(validation).contains("Incorrect value for 'web_services_security.keystores': must contain only unique paths. Duplicates: ./wss-keystores/a.p12."));
	}

	@Test
	public void duplicateRendezvousPoliciesInOneScenarioAreRejected() throws IOException, URISyntaxException {
		final Validation validation = validate("duplicate-rendezvous-policies");
		final String message = message(validation);
		assertFalse(validation.isValid());
		assertTrue(message, message.contains(duplicateNamesMessage("scenarios[0].rendezvous_policies", "rdv")));
	}

	@Test
	public void sameRendezvousPolicyNameInTwoScenariosIsAllowed() throws IOException, URISyntaxException {
		final String message = message(validate("rendezvous-policy-in-two-scenarios"));
		assertFalse(message, message.contains(NOT_UNIQUE));
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
		final String message = message(validate("empty-list-item"));
		assertFalse(message, message.contains(NOT_UNIQUE));
	}

	@Test
	public void listsLeftAsIsStillAcceptDuplicates() throws IOException, URISyntaxException {
		final String message = message(validate("lists-left-as-is"));
		assertFalse(message, message.contains(NOT_UNIQUE));
	}

	@Test
	public void messageParametersAreNotInterpolated() throws IOException, URISyntaxException {
		final Validation validation = validate("uninterpolated-message-parameters");
		final String message = message(validation);
		assertFalse(validation.isValid());
		assertTrue(message, message.contains(duplicateNamesMessage("servers", "${1+1}{x}")));
	}
}
