package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * LOAD-40185: every field documented in {@code neoload-project/doc/v3} that exists in a published
 * schema must have a JSON Schema {@code description} next to that field.
 */
public class IOSchemaFieldDescriptionsTest {

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private static JsonNode schema30;
	private static JsonNode schema31;

	@BeforeClass
	public static void loadSchemas() throws IOException, URISyntaxException {
		Path schemasDir = locateSchemasDir();
		schema30 = JSON_MAPPER.readTree(schemasDir.resolve("v3.0/as-code.schema.json").toFile());
		schema31 = JSON_MAPPER.readTree(schemasDir.resolve("v3.1/as-code.schema.json").toFile());
	}

	@Test
	public void rootProjectFieldsAreDocumentedInBothSchemas() {
		assertDescription(schema30, "/properties/name", "name of the project");
		assertDescription(schema30, "/properties/$schema", "JSON Schema");
		assertDescription(schema30, "/properties/schemaVersion", "Schema contract version");

		assertDescription(schema31, "/properties/name", "name of the project");
		assertDescription(schema31, "/properties/schemaVersion", "3.1");
	}

	@Test
	public void requestFieldsMatchReadme() {
		String request = "/definitions/user_paths/actions/request/properties";
		for (JsonNode schema : new JsonNode[] {schema30, schema31}) {
			assertDescription(schema, request + "/followRedirects", "redirection");
			assertDescription(schema, request + "/sla_profile", "SLA");
		}
		assertDescription(schema31, request + "/bodybinary", "base64");
		assertDescription(schema31, request + "/parts", "multipart");
		assertDescription(schema31, request + "/duration_assertion", "duration");
		assertDescription(schema31, "/definitions/user_paths/part/properties/source_filename", "project folder");
	}

	@Test
	public void userPathAndScenarioFieldsMatchReadme() {
		assertDescription(schema31, "/definitions/user_paths/user_path/properties/assertions", "assertions");
		assertDescription(schema31, "/definitions/scenario/monitoring/properties/before", "before");
		assertDescription(schema31, "/definitions/scenario/apm/properties/dynatrace_tags", "Dynatrace");
		assertDescription(schema31, "/definitions/user_paths/actions/web_page/properties/think_time", "think time");
		assertDescription(schema31, "/definitions/user_paths/actions/variable_modifier/properties/variable_name", "raw name");
	}

	@Test
	public void publishedSchemasExposeReadmeDescriptions() {
		assertTrue("3.0 schema should document readme fields", countTextualDescriptions(schema30) >= 110);
		assertTrue("3.1 schema should document readme fields", countTextualDescriptions(schema31) >= 220);
	}

	private static int countTextualDescriptions(JsonNode node) {
		if (node == null) {
			return 0;
		}
		int count = 0;
		if (node.isObject()) {
			JsonNode description = node.get("description");
			if (description != null && description.isTextual() && !description.asText().isEmpty()) {
				count++;
			}
			Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
			while (fields.hasNext()) {
				count += countTextualDescriptions(fields.next().getValue());
			}
		} else if (node.isArray()) {
			for (JsonNode child : node) {
				count += countTextualDescriptions(child);
			}
		}
		return count;
	}

	private static void assertDescription(JsonNode schema, String pointer, String mustContain) {
		JsonNode target = schema.at(pointer);
		assertFalse("Missing schema node " + pointer, target.isMissingNode());
		JsonNode description = target.get("description");
		assertNotNull(pointer + " must declare a JSON Schema description", description);
		assertTrue(pointer + " description must be a string", description.isTextual());
		assertTrue(pointer + " description \"" + description.asText() + "\" should mention \"" + mustContain + "\"",
				description.asText().toLowerCase().contains(mustContain.toLowerCase()));
	}

	private static Path locateSchemasDir() throws URISyntaxException {
		URL testClassesUrl = IOSchemaFieldDescriptionsTest.class.getProtectionDomain().getCodeSource().getLocation();
		Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
		return moduleDir.resolve("../schemas").normalize();
	}
}
