package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

/**
 * Keeps the published v3.1 schema and the runtime copy aligned with the model on empty collections:
 * a container, a transaction and a scenario may be empty while a project is designed, whereas the
 * step lists the model still requires (loop, while, fork, web_page, switch case) must hold at least one step.
 */
public class IOSchemaEmptyCollectionsTest {

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private static final String ONE_STEP = "[{\"delay\":\"1s\"}]";
	private static final String NO_STEP = "[]";

	private static JsonSchema schema31;
	private static JsonSchema latestSchema;

	@BeforeClass
	public static void loadSchemas() throws URISyntaxException {
		final URL testClassesUrl = IOSchemaEmptyCollectionsTest.class.getProtectionDomain().getCodeSource().getLocation();
		final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
		schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
				.getSchema(moduleDir.resolve("../schemas/v3.1/as-code.schema.json").normalize().toUri());
		latestSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
				.getSchema(IOSchemaEmptyCollectionsTest.class.getResource("/as-code.latest.schema.json").toURI());
	}

	@Test
	public void emptyContainersAreAccepted() throws IOException {
		for (final JsonSchema schema : new JsonSchema[] {schema31, latestSchema}) {
			assertValid(schema, "{\"name\":\"P\",\"user_paths\":[{\"name\":\"U\",\"init\":{},\"actions\":{},\"end\":{}}]}");
			assertValid(schema, userPathWithStep("{\"transaction\":{\"name\":\"T\"}}"));
			assertValid(schema, "{\"name\":\"P\",\"scenarios\":[{\"name\":\"S\"}]}");
		}
	}

	@Test
	public void loopWhileAndCaseRequireOneStep() throws IOException {
		for (final JsonSchema schema : new JsonSchema[] {schema31, latestSchema}) {
			assertRequiresOneStep(schema, steps -> "{\"loop\":{\"count\":2,\"steps\":" + steps + "}}");
			assertRequiresOneStep(schema, steps -> "{\"while\":{\"steps\":" + steps + "}}");
			assertRequiresOneStep(schema, steps -> "{\"switch\":{\"value\":\"x\",\"case\":[{\"value\":\"a\",\"steps\":" + steps + "}],\"default\":{\"steps\":" + ONE_STEP + "}}}");
		}
	}

	@Test
	public void forkAndWebPageRequireOneStep() throws IOException {
		assertRequiresOneStep(schema31, steps -> "{\"fork\":{\"steps\":" + steps + "}}");
		assertRequiresOneStep(schema31, steps -> "{\"web_page\":{\"steps\":" + steps + "}}");
	}

	private static void assertRequiresOneStep(final JsonSchema schema, final UnaryOperator<String> stepWithSteps) throws IOException {
		assertValid(schema, userPathWithStep(stepWithSteps.apply(ONE_STEP)));
		final String emptyDocument = userPathWithStep(stepWithSteps.apply(NO_STEP));
		assertFalse("Expected a violation for " + emptyDocument, validate(schema, emptyDocument).isEmpty());
	}

	private static void assertValid(final JsonSchema schema, final String document) throws IOException {
		assertEquals("Unexpected violations for " + document, Collections.emptySet(), validate(schema, document));
	}

	private static Set<ValidationMessage> validate(final JsonSchema schema, final String document) throws IOException {
		return schema.validate(JSON_MAPPER.readTree(document));
	}

	private static String userPathWithStep(final String step) {
		return "{\"name\":\"P\",\"user_paths\":[{\"name\":\"U\",\"actions\":{\"steps\":[" + step + "]}}]}";
	}
}
