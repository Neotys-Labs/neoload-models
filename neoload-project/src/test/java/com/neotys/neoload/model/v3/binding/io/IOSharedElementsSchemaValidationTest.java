package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Schema-level coverage for {@code shared_elements} / {@code shared_element}: restricted to
 * {@code transaction}, {@code loop}, {@code while} and {@code fork} bodies, and declared only in
 * the 3.1 contract. The reader itself accepts every construct it knows about regardless of the
 * declared {@code schemaVersion} (see FEATURE-COVERAGE.md); these two concerns are gated by the
 * published JSON Schemas, exercised here exactly like {@link IORequestSchemaValidationTest}.
 */
public class IOSharedElementsSchemaValidationTest {

	private static final ObjectMapper YAML_MAPPER = new YAMLMapper();

	private static JsonSchema schema30;
	private static JsonSchema schema31;

	@BeforeClass
	public static void loadSchemas() throws URISyntaxException {
		final Path schemasDir = locateSchemasDir();
		final Path schema30Path = schemasDir.resolve("v3.0/as-code.schema.json");
		final Path schema31Path = schemasDir.resolve("v3.1/as-code.schema.json");
		schema30 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schema30Path.toUri());
		schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schema31Path.toUri());
	}

	@Test
	public void sharedElementsWithFourContainerTypesIsValidAgainst3Dot1() throws IOException, URISyntaxException {
		final JsonNode node = readFixture("test-shared-elements-four-container-types.yaml");
		assertTrue("valid shared_elements entry must pass the 3.1 schema", schema31.validate(node).isEmpty());
	}

	@Test
	public void sharedElementsRejectsUnsupportedType() throws IOException, URISyntaxException {
		final JsonNode node = readFixture("test-shared-elements-unsupported-type.yaml");
		assertFalse("a shared_elements entry of an unsupported type ('request') must be rejected",
				schema31.validate(node).isEmpty());
	}

	@Test
	public void sharedElementsRejectsMissingNameOnLoopWhileFork() throws IOException, URISyntaxException {
		final JsonNode loopNode = readFixture("test-shared-elements-missing-name-loop.yaml");
		assertFalse("a shared_elements loop entry without a name must be rejected",
				schema31.validate(loopNode).isEmpty());

		final JsonNode whileNode = readFixture("test-shared-elements-missing-name-while.yaml");
		assertFalse("a shared_elements while entry without a name must be rejected",
				schema31.validate(whileNode).isEmpty());

		final JsonNode forkNode = readFixture("test-shared-elements-missing-name-fork.yaml");
		assertFalse("a shared_elements fork entry without a name must be rejected",
				schema31.validate(forkNode).isEmpty());
	}

	@Test
	public void sharedElementsIsRejectedBelow3Dot1() throws IOException, URISyntaxException {
		final JsonNode node = readFixture("test-shared-elements-rejected-below-3-1.yaml");
		assertFalse("shared_elements must not be accepted by the 3.0 schema", schema30.validate(node).isEmpty());
	}

	@Test
	public void sharedElementReferenceStepIsRejectedBelow3Dot1() throws IOException, URISyntaxException {
		final JsonNode node = readFixture("test-shared-element-reference-step-rejected-below-3-1.yaml");
		assertFalse("a shared_element reference step must not be accepted by the 3.0 schema",
				schema30.validate(node).isEmpty());
	}

	private static JsonNode readFixture(final String resourceName) throws IOException, URISyntaxException {
		final URL fixtureUrl = IOSharedElementsSchemaValidationTest.class.getClassLoader().getResource(resourceName);
		assertNotNull("Missing classpath resource " + resourceName, fixtureUrl);
		return YAML_MAPPER.readTree(new File(fixtureUrl.toURI()));
	}

	private static Path locateSchemasDir() throws URISyntaxException {
		final java.net.URL testClassesUrl = IOSharedElementsSchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
		final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
		return moduleDir.resolve("../schemas").normalize();
	}
}
