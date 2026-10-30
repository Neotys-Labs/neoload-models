package com.neotys.neoload.model.v3.binding.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Checks the documentation annotations of the as-code v3.1 JSON Schema ({@code schemas/v3.1/as-code.schema.json}):
 * every definition and property carries a description, the main elements carry examples, and every example
 * validates against the subschema that declares it.
 */
public class IOSchema3Dot1DocumentationTest {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();
    private static final List<String> SCHEMA_MAPS = List.of("properties", "definitions", "patternProperties");
    private static final List<String> MAIN_ELEMENTS = List.of(
            "variables/constant", "variables/file", "variables/counter", "variables/random_number",
            "variables/random_string", "variables/password", "variables/date", "variables/current_date",
            "variables/list", "variables/sql", "variables/javascript", "variables/random_uuid",
            "variables/shared_queue", "variables/secret_vault",
            "user_paths/actions/transaction", "user_paths/actions/request", "user_paths/actions/soap_request",
            "user_paths/actions/duration_action", "user_paths/actions/javascript", "user_paths/actions/if",
            "user_paths/actions/while", "user_paths/actions/loop", "user_paths/actions/switch",
            "user_paths/actions/custom_action", "user_paths/actions/rendezvous",
            "user_paths/actions/go_to_next_iteration", "user_paths/actions/stop_vu", "user_paths/actions/fork",
            "user_paths/actions/wait_until", "user_paths/actions/variable_modifier",
            "user_paths/actions/debug_logger", "user_paths/actions/try_catch", "user_paths/actions/web_page",
            "user_paths/actions/websocket_channel", "user_paths/actions/websocket_request",
            "populations/population", "scenario", "sla");

    private static JsonNode schemaNode;
    private static JsonSchema schema;

    @BeforeClass
    public static void loadSchema() throws URISyntaxException, IOException {
        final URL testClassesUrl = IOSchema3Dot1DocumentationTest.class.getProtectionDomain().getCodeSource().getLocation();
        final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        final Path schemaPath = moduleDir.resolve("../schemas/v3.1/as-code.schema.json").normalize();
        schemaNode = JSON_MAPPER.readTree(schemaPath.toFile());
        schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemaPath.toUri());
    }

    @Test
    public void everyDefinitionAndPropertyHasDescription() {
        final List<String> missing = new ArrayList<>();
        collectMissingDescriptions(schemaNode, "", missing);
        Assert.assertTrue(missing.size() + " definition(s) or property(ies) without description:\n" + String.join("\n", missing),
                missing.isEmpty());
    }

    private static void collectMissingDescriptions(final JsonNode node, final String pointer, final List<String> missing) {
        if (node.isObject()) {
            final Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                final Map.Entry<String, JsonNode> field = fields.next();
                final String childPointer = pointer + "/" + field.getKey();
                if (SCHEMA_MAPS.contains(field.getKey()) && field.getValue().isObject()) {
                    collectMissingDescriptionsOfMap(field.getValue(), childPointer, missing);
                } else {
                    collectMissingDescriptions(field.getValue(), childPointer, missing);
                }
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                collectMissingDescriptions(node.get(i), pointer + "/" + i, missing);
            }
        }
    }

    /**
     * A boolean subschema such as {@code "mapping_id": false} cannot carry annotations, so only object subschemas are checked.
     */
    private static void collectMissingDescriptionsOfMap(final JsonNode map, final String pointer, final List<String> missing) {
        final Iterator<Map.Entry<String, JsonNode>> entries = map.fields();
        while (entries.hasNext()) {
            final Map.Entry<String, JsonNode> entry = entries.next();
            final String entryPointer = pointer + "/" + entry.getKey();
            final JsonNode description = entry.getValue().get("description");
            if (entry.getValue().isObject() && (description == null || !description.isTextual() || description.asText().isBlank())) {
                missing.add(entryPointer);
            }
            collectMissingDescriptions(entry.getValue(), entryPointer, missing);
        }
    }

    @Test
    public void mainElementsCarryExamples() {
        final List<String> withoutExample = new ArrayList<>();
        for (final String element : MAIN_ELEMENTS) {
            final JsonNode examples = schemaNode.at("/definitions/" + element + "/examples");
            if (!examples.isArray() || examples.isEmpty()) {
                withoutExample.add(element);
            }
        }
        Assert.assertTrue("Main element(s) without example: " + withoutExample, withoutExample.isEmpty());
    }

    @Test
    public void everyExampleValidatesAgainstItsSubschema() {
        final List<String> failures = new ArrayList<>();
        collectExampleFailures(schemaNode, "", failures);
        Assert.assertTrue(failures.size() + " example(s) invalid:\n" + String.join("\n", failures), failures.isEmpty());
    }

    private static void collectExampleFailures(final JsonNode node, final String pointer, final List<String> failures) {
        if (node.isObject()) {
            final JsonNode examples = node.get("examples");
            if (examples != null && examples.isArray() && pointer.startsWith("/definitions/")) {
                validateExamples(examples, pointer, failures);
            }
            node.fields().forEachRemaining(field -> collectExampleFailures(field.getValue(), pointer + "/" + field.getKey(), failures));
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                collectExampleFailures(node.get(i), pointer + "/" + i, failures);
            }
        }
    }

    private static void validateExamples(final JsonNode examples, final String pointer, final List<String> failures) {
        final ObjectNode wrapper = JSON_MAPPER.createObjectNode();
        wrapper.put("$schema", schemaNode.get("$schema").asText());
        wrapper.put("$ref", "#" + pointer);
        wrapper.set("definitions", schemaNode.get("definitions"));
        final JsonSchema subschema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(wrapper);
        for (final JsonNode example : examples) {
            if (!subschema.validate(example).isEmpty()) {
                failures.add(pointer + ": " + example);
            }
        }
    }

    @Test
    public void frameworksIsRejectedAtTheRoot() throws IOException {
        final JsonNode project = YAML_MAPPER.readTree("name: MyProject\nframeworks:\n- name: MyFramework\n");
        Assert.assertFalse("Expected 'frameworks' to fail schema validation", schema.validate(project).isEmpty());
        Assert.assertFalse("The schema must not declare 'frameworks'", schemaNode.toString().contains("\"frameworks\""));
    }
}
