package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Schema-level coverage for published request contracts (3.0 and 3.1):
 * {@code followRedirects} is declared, {@code name} is not (NLG ignores it),
 * and {@code method} is any string (default GET).
 */
public class IORequestSchemaValidationTest {

    private static final String ALL_METHODS_FIXTURE = "test-request-all-methods.yaml";
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private static JsonSchema SCHEMA_3_0;
    private static JsonNode SCHEMA_3_0_TREE;
    private static JsonSchema SCHEMA_3_1;
    private static JsonNode SCHEMA_3_1_TREE;
    private static JsonNode ALL_METHODS_DOCUMENT;

    @BeforeClass
    public static void loadSchemas() throws IOException, URISyntaxException {
        Path schemasDir = locateSchemasDir();
        Path schema30Path = schemasDir.resolve("v3.0/as-code.schema.json");
        Path schema31Path = schemasDir.resolve("v3.1/as-code.schema.json");
        SCHEMA_3_0_TREE = JSON_MAPPER.readTree(schema30Path.toFile());
        SCHEMA_3_1_TREE = JSON_MAPPER.readTree(schema31Path.toFile());
        SCHEMA_3_0 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schema30Path.toUri());
        SCHEMA_3_1 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schema31Path.toUri());

        URL fixtureUrl = IORequestSchemaValidationTest.class.getClassLoader().getResource(ALL_METHODS_FIXTURE);
        assertNotNull("Missing classpath resource " + ALL_METHODS_FIXTURE, fixtureUrl);
        ALL_METHODS_DOCUMENT = YAML_MAPPER.readTree(new File(fixtureUrl.toURI()));
    }

    @Test
    public void requestSchemaDoesNotDeclareName() {
        assertFalse("3.0 request must not declare name (ignored by NLG)",
                requestProperties(SCHEMA_3_0_TREE).has("name"));
        assertFalse("3.1 request must not declare name (ignored by NLG)",
                requestProperties(SCHEMA_3_1_TREE).has("name"));
    }

    @Test
    public void requestMethodHasNoEnum() {
        assertMethodIsOpenString(SCHEMA_3_0_TREE, "3.0");
        assertMethodIsOpenString(SCHEMA_3_1_TREE, "3.1");
    }

    @Test
    public void requestSchemaDeclaresFollowRedirects() {
        assertFollowRedirects(SCHEMA_3_0_TREE);
        assertFollowRedirects(SCHEMA_3_1_TREE);
    }

    @Test
    public void allHttpMethodsFixtureIsValidAgainst3Dot0And3Dot1() {
        assertValid("3.0", SCHEMA_3_0, ALL_METHODS_DOCUMENT);
        assertValid("3.1", SCHEMA_3_1, ALL_METHODS_DOCUMENT);
    }

    @Test
    public void followRedirectsRejectsNonBoolean() throws IOException {
        JsonNode node = YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - request:\n"
                        + "        url: http://www.neotys.com/select\n"
                        + "        followRedirects: not-a-boolean\n");
        assertFalse("followRedirects must be a boolean in 3.0", SCHEMA_3_0.validate(node).isEmpty());
        assertFalse("followRedirects must be a boolean in 3.1", SCHEMA_3_1.validate(node).isEmpty());
    }

    private static JsonNode requestProperties(JsonNode schemaTree) {
        JsonNode properties = schemaTree.at("/definitions/user_paths/actions/request/properties");
        assertTrue("request.properties must exist", properties.isObject());
        return properties;
    }

    private static void assertMethodIsOpenString(JsonNode schemaTree, String label) {
        JsonNode method = requestProperties(schemaTree).get("method");
        assertNotNull(label + " request.method must be declared", method);
        assertEquals(label, "string", method.get("type").asText());
        assertEquals(label, "GET", method.get("default").asText());
        assertFalse(label + " request.method must not declare an enum (any string is allowed)",
                method.has("enum"));
    }

    private static void assertFollowRedirects(JsonNode schemaTree) {
        JsonNode followRedirects = requestProperties(schemaTree).get("followRedirects");
        assertNotNull("request.properties.followRedirects must be declared", followRedirects);
        assertEquals("boolean", followRedirects.get("type").asText());
        assertFalse(followRedirects.get("default").asBoolean());
    }

    private static void assertValid(String label, JsonSchema schema, JsonNode node) {
        Set<ValidationMessage> errors = schema.validate(node);
        if (!errors.isEmpty()) {
            fail(label + " schema rejected all-methods fixture:\n  "
                    + errors.stream()
                            .map(ValidationMessage::getMessage)
                            .sorted()
                            .collect(Collectors.joining("\n  ")));
        }
    }

    private static Path locateSchemasDir() throws URISyntaxException {
        URL testClassesUrl = IORequestSchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        return moduleDir.resolve("../schemas").normalize();
    }
}
