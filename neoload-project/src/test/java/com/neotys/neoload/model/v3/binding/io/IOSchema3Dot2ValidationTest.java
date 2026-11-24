package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Validates every YAML and JSON fixture in {@code neoload-project/src/test/resources} against the as-code v3.2 JSON
 * Schema ({@code schemas/v3.2/as-code.schema.json}), as {@link IOSchema3Dot1ValidationTest} does for v3.1.
 *
 * <p>Fixtures declaring another {@code schemaVersion} are validated by the schema of their version only. The
 * fixtures excluded from the v3.1 validation are excluded here too, for the same reasons.</p>
 *
 * <p>Schema 3.1 is frozen: the constructs added since are in 3.2 only, and their fixtures declare
 * {@code schemaVersion: "3.2"}.</p>
 */
public class IOSchema3Dot2ValidationTest {

    private static final String SCHEMA_VERSION = "3.2";
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private static Path schemasDir;
    private static JsonSchema schema;

    @BeforeClass
    public static void loadSchema() throws URISyntaxException {
        // Resolve schema relative to the module root: target/test-classes -> target -> neoload-project -> ../schemas/
        final URL testClassesUrl = IOSchema3Dot2ValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        schemasDir = moduleDir.resolve("../schemas").normalize();
        schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(schemasDir.resolve("v3.2/as-code.schema.json").toUri());
    }

    @Test
    public void allFixturesConformToSchema() throws IOException, URISyntaxException {
        final URL anchor = getClass().getClassLoader().getResource("test-descriptor-only-required.yaml");
        Assert.assertNotNull(anchor);
        final Path resourceDir = Paths.get(anchor.toURI()).getParent();

        final List<String> failures = new ArrayList<>();
        try (Stream<Path> files = Files.list(resourceDir)) {
            files.filter(Files::isRegularFile)
                 .filter(p -> {
                     final String name = p.getFileName().toString();
                     return name.endsWith(".yaml") || name.endsWith(".json");
                 })
                 .filter(p -> !IOSchema3Dot1ValidationTest.EXCLUDED_FIXTURES.contains(p.getFileName().toString()))
                 .sorted()
                 .forEach(file -> {
                     final String fileName = file.getFileName().toString();
                     try {
                         final ObjectMapper mapper = fileName.endsWith(".yaml") ? YAML_MAPPER : JSON_MAPPER;
                         final JsonNode node = mapper.readTree(file.toFile());
                         if (IOSchema3Dot1ValidationTest.declaresAnotherSchemaVersion(node, SCHEMA_VERSION)) {
                             return;
                         }
                         final Set<ValidationMessage> errors = schema.validate(node);
                         if (!errors.isEmpty()) {
                             failures.add(fileName + ":\n  " + errors.stream()
                                     .map(ValidationMessage::getMessage)
                                     .sorted()
                                     .collect(Collectors.joining("\n  ")));
                         }
                     } catch (final IOException e) {
                         failures.add(fileName + ": parse error: " + e.getMessage());
                     }
                 });
        }

        if (!failures.isEmpty()) {
            fail(failures.size() + " fixture(s) failed schema validation:\n\n" + String.join("\n\n", failures));
        }
    }

    @Test
    public void schemaVersion32ConformsTo32AndNotTo31() throws IOException {
        final JsonNode node = YAML_MAPPER.readTree(getClass().getClassLoader().getResource("test-schema-version-3-2.yaml"));
        assertTrue(schema.validate(node).toString(), schema.validate(node).isEmpty());
        assertFalse(schema31().validate(node).isEmpty());
    }

    @Test
    public void schemaVersion31DoesNotConformTo32() throws IOException {
        final JsonNode node = YAML_MAPPER.readTree("schemaVersion: \"3.1\"\nname: MyProject\n");

        assertFalse(schema.validate(node).isEmpty());
    }

    @Test
    public void populationBrowserAndCacheAreIn32Only() throws IOException {
        final JsonNode node = YAML_MAPPER.readTree(getClass().getClassLoader().getResource("test-populations-browser-cache.yaml"));
        final ObjectNode as31 = node.deepCopy();
        as31.put("schemaVersion", "3.1");

        assertTrue(schema.validate(node).toString(), schema.validate(node).isEmpty());
        final String errors31 = schema31().validate(as31).toString();
        assertTrue(errors31, errors31.contains("browser") && errors31.contains("cache"));
    }

    @Test
    public void serverSettingsAreIn32Only() throws IOException {
        final JsonNode node = YAML_MAPPER.readTree(getClass().getClassLoader().getResource("test-servers-all-settings.yaml"));
        final ObjectNode as31 = node.deepCopy();
        as31.put("schemaVersion", "3.1");

        assertTrue(schema.validate(node).toString(), schema.validate(node).isEmpty());
        final String errors31 = schema31().validate(as31).toString();
        assertTrue(errors31, errors31.contains("authentications") && errors31.contains("url_rewriting") && errors31.contains("type"));
    }

    /** The 3.2 schema rejects the servers the Java reader rejects, except the duplicate realm, which a schema can't check. */
    @Test
    public void invalidServersDoNotConformTo32() throws IOException {
        final List<String> accepted = new ArrayList<>();
        for (final String fixture : List.of("test-servers-unknown-key", "test-servers-description", "test-servers-nested-host",
                "test-servers-tcp-with-scheme", "test-servers-tcp-without-port", "test-servers-port-too-high",
                "test-servers-port-zero", "test-servers-two-authentication-forms",
                "test-servers-url-rewriting-separator-without-in-path",
                "test-servers-url-rewriting-without-session-argument", "test-servers-unknown-preferred-protocol",
                "test-servers-basic-authentication-empty-password")) {
            final JsonNode node = YAML_MAPPER.readTree(getClass().getClassLoader().getResource(fixture + ".yaml"));
            if (schema.validate(node).isEmpty()) {
                accepted.add(fixture);
            }
        }

        assertTrue("Accepted by the 3.2 schema: " + accepted, accepted.isEmpty());
    }

    private static JsonSchema schema31() {
        return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(schemasDir.resolve("v3.1/as-code.schema.json").toUri());
    }
}
