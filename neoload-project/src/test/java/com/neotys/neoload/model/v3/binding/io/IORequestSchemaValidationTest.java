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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Schema-level coverage for request properties that the Java model already supports
 * ({@code name}, {@code followRedirects}, {@code method: CUSTOM}) but that were missing
 * from {@code as-code.latest.schema.json}.
 */
public class IORequestSchemaValidationTest {

    private static final String SCHEMA_RESOURCE = "as-code.latest.schema.json";
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private static JsonSchema SCHEMA;
    private static JsonNode SCHEMA_TREE;

    @BeforeClass
    public static void loadSchema() throws IOException, URISyntaxException {
        URL schemaUrl = IORequestSchemaValidationTest.class.getClassLoader().getResource(SCHEMA_RESOURCE);
        assertNotNull("Missing classpath resource " + SCHEMA_RESOURCE, schemaUrl);
        SCHEMA = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaUrl.toURI());
        try (InputStream in = schemaUrl.openStream()) {
            SCHEMA_TREE = JSON_MAPPER.readTree(in);
        }
    }

    @Test
    public void requestSchemaDeclaresNameFollowRedirectsAndCustomMethod() {
        JsonNode request = SCHEMA_TREE.at("/definitions/user_paths/actions/request");
        assertTrue("request definition must exist", request.isObject());

        JsonNode properties = request.get("properties");
        assertNotNull(properties);

        JsonNode name = properties.get("name");
        assertNotNull("request.properties.name must be declared", name);
        assertEquals("#/definitions/common/text", name.get("$ref").asText());

        JsonNode followRedirects = properties.get("followRedirects");
        assertNotNull("request.properties.followRedirects must be declared", followRedirects);
        assertEquals("boolean", followRedirects.get("type").asText());
        assertFalse(followRedirects.get("default").asBoolean());

        JsonNode method = properties.get("method");
        assertNotNull(method);
        assertTrue("method enum must include CUSTOM", enumValues(method).anyMatch("CUSTOM"::equals));
    }

    @Test
    public void yamlWithNameFollowRedirectsAndCustomMethodIsValid() throws IOException {
        assertValid(YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - request:\n"
                        + "        name: MyNamedRequest\n"
                        + "        url: http://www.neotys.com/select\n"
                        + "        method: CUSTOM\n"
                        + "        followRedirects: true\n"));
    }

    @Test
    public void jsonWithNameFollowRedirectsAndCustomMethodIsValid() throws IOException {
        assertValid(JSON_MAPPER.readTree(
                "{\"name\":\"MyProject\",\"user_paths\":[{\"name\":\"MyUserPath\","
                        + "\"actions\":{\"steps\":[{\"request\":{"
                        + "\"name\":\"MyNamedRequest\","
                        + "\"url\":\"http://www.neotys.com/select\","
                        + "\"method\":\"CUSTOM\","
                        + "\"followRedirects\":false}}]}}]}"));
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
        assertFalse("followRedirects must be a boolean", SCHEMA.validate(node).isEmpty());
    }

    @Test
    public void schema3Dot1MethodEnumIncludesCustom() throws IOException, URISyntaxException {
        URL testClassesUrl = IORequestSchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        Path schemaPath = moduleDir.resolve("../schemas/v3.1/as-code.schema.json").normalize();
        JsonNode schema31 = JSON_MAPPER.readTree(schemaPath.toFile());

        JsonNode method = schema31.at("/definitions/user_paths/actions/request/properties/method");
        assertTrue("3.1 request.method must exist", method.isObject());
        assertTrue("3.1 method enum must include CUSTOM", enumValues(method).anyMatch("CUSTOM"::equals));

        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemaPath.toUri());
        JsonNode valid = YAML_MAPPER.readTree(
                "schemaVersion: \"3.1\"\n"
                        + "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - request:\n"
                        + "        name: MyNamedRequest\n"
                        + "        url: http://www.neotys.com/select\n"
                        + "        method: CUSTOM\n"
                        + "        followRedirects: true\n");
        Set<ValidationMessage> errors = schema.validate(valid);
        if (!errors.isEmpty()) {
            fail("3.1 schema rejected name/followRedirects/CUSTOM:\n  "
                    + errors.stream()
                            .map(ValidationMessage::getMessage)
                            .sorted()
                            .collect(Collectors.joining("\n  ")));
        }
    }

    @Test
    public void methodAcceptsStandardAndCustomEnumValues() throws IOException {
        for (String method : new String[] {"GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS", "TRACE", "CUSTOM"}) {
            assertValid(YAML_MAPPER.readTree(
                    "name: MyProject\n"
                            + "user_paths:\n"
                            + "- name: MyUserPath\n"
                            + "  actions:\n"
                            + "    steps:\n"
                            + "    - request:\n"
                            + "        url: http://www.neotys.com/select\n"
                            + "        method: " + method + "\n"));
        }
    }

    private static Stream<String> enumValues(JsonNode method) {
        JsonNode enumNode = method.get("enum");
        assertNotNull("method must declare an enum", enumNode);
        assertTrue(enumNode.isArray());
        return StreamSupport.stream(enumNode.spliterator(), false).map(JsonNode::asText);
    }

    private static void assertValid(JsonNode node) {
        Set<ValidationMessage> errors = SCHEMA.validate(node);
        if (!errors.isEmpty()) {
            fail("Unexpected schema errors:\n  "
                    + errors.stream()
                            .map(ValidationMessage::getMessage)
                            .sorted()
                            .collect(Collectors.joining("\n  ")));
        }
    }
}
