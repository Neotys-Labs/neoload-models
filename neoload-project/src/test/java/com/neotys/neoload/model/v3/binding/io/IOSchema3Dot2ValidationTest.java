package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
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
        final JsonSchema schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(schemasDir.resolve("v3.1/as-code.schema.json").toUri());

        assertTrue(schema.validate(node).toString(), schema.validate(node).isEmpty());
        assertFalse(schema31.validate(node).isEmpty());
    }

    @Test
    public void schemaVersion31DoesNotConformTo32() throws IOException {
        final JsonNode node = YAML_MAPPER.readTree("schemaVersion: \"3.1\"\nname: MyProject\n");

        assertFalse(schema.validate(node).isEmpty());
    }

    @Test
    public void nonBooleanApplyGlobalAssertionsOnCustomActionsFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected a non-boolean 'global_assertions: apply_on_custom_actions' to fail schema validation",
                validate("test-project-settings-global-assertions-invalid").isEmpty());
    }

    @Test
    public void dottedAndNestedProjectSettingFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected 'qtest.enabled' declared with 'qtest: enabled' to fail schema validation",
                validate("test-project-settings-keys-conflict").isEmpty());
    }

    @Test
    public void dottedGlobalAssertionsProjectSettingFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected the dotted 'global_assertions.content_types' to fail schema validation",
                validate("test-project-settings-global-assertions-dotted").isEmpty());
    }

    @Test
    public void globalAssertionsProjectSettingsConformTo32AndNotTo31() throws IOException {
        final JsonNode node = YAML_MAPPER.readTree(getClass().getClassLoader().getResource("test-project-settings-global-assertions.yaml"));
        final JsonSchema schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(schemasDir.resolve("v3.1/as-code.schema.json").toUri());

        assertTrue(schema.validate(node).toString(), schema.validate(node).isEmpty());
        assertFalse(schema31.validate(node).isEmpty());
    }

    private Set<ValidationMessage> validate(final String fixture) throws IOException {
        return schema.validate(YAML_MAPPER.readTree(getClass().getClassLoader().getResource(fixture + ".yaml")));
    }
}
