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
    private static final String UNKNOWN_KEY = "zz_unknown_key";
    /** The lists whose items are checked by the loader for unique names. */
    private static final List<String> UNIQUE_NAME_LISTS = List.of(
            "/properties/servers", "/properties/variables", "/properties/user_paths", "/properties/populations",
            "/properties/scenarios", "/properties/sla_profiles", "/properties/shared_elements",
            "/definitions/scenario/properties/populations",
            "/definitions/web_services_security/properties/request_profiles",
            "/definitions/web_services_security/properties/response_profiles");
    /** The fields whose maximum is checked by the loader against the minimum. */
    private static final List<String> MIN_MAX_FIELDS = List.of(
            "/definitions/variables/random_number/allOf/1/properties/max",
            "/definitions/variables/random_string/allOf/1/properties/max_length",
            "/definitions/populations/rampup_load/properties/max_users",
            "/definitions/user_paths/container/properties/pacing",
            "/definitions/user_paths/actions/transaction/properties/pacing",
            "/definitions/user_paths/case/properties/pacing",
            "/definitions/user_paths/actions/duration_object/oneOf/1/properties/max");

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
        final List<String> failures = new ArrayList<>();
        for (final Path file : fixturesValidatedBy32()) {
            final Set<ValidationMessage> errors = schema.validate(read(file));
            if (!errors.isEmpty()) {
                failures.add(file.getFileName() + ":\n  " + errors.stream()
                        .map(ValidationMessage::getMessage)
                        .sorted()
                        .collect(Collectors.joining("\n  ")));
            }
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
    public void nonBooleanApplyContentAssertionsOnCustomActionsFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected a non-boolean 'runtime: responses: content_assertions: apply_on_custom_actions' to fail schema validation",
                validate("test-project-settings-content-assertions-invalid").isEmpty());
    }

    @Test
    public void dottedAndNestedProjectSettingFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected 'qtest.enabled' declared with 'qtest: enabled' to fail schema validation",
                validate("test-project-settings-keys-conflict").isEmpty());
    }

    @Test
    public void dottedRuntimeProjectSettingFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected the dotted 'runtime.responses.content_assertions.content_types' to fail schema validation",
                validate("test-project-settings-content-assertions-dotted").isEmpty());
    }

    @Test
    public void runtimeProjectSettingsConformTo32AndNotTo31() throws IOException {
        final JsonNode node = YAML_MAPPER.readTree(getClass().getClassLoader().getResource("test-project-settings-content-assertions.yaml"));
        final JsonSchema schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(schemasDir.resolve("v3.1/as-code.schema.json").toUri());

        assertTrue(schema.validate(node).toString(), schema.validate(node).isEmpty());
        assertFalse(schema31.validate(node).isEmpty());
    }

    @Test
    public void uniquenessRulesAreStatedInTheListDescriptions() throws IOException {
        final JsonNode root = JSON_MAPPER.readTree(schemasDir.resolve("v3.2/as-code.schema.json").toFile());
        for (final String list : UNIQUE_NAME_LISTS) {
            final String description = root.at(list + "/description").asText();
            assertTrue(list + ": " + description, description.contains("Names must be unique within the list."));
        }
        final String keystores = root.at("/definitions/web_services_security/properties/keystores/description").asText();
        assertTrue(keystores, keystores.contains("Paths must be unique within the list."));
        final String cases = root.at("/definitions/user_paths/actions/switch/properties/case/description").asText();
        assertTrue(cases, cases.contains("Case values must be unique within the list."));
    }

    @Test
    public void minMaxRulesAreStatedInTheFieldDescriptions() throws IOException {
        final JsonNode root = JSON_MAPPER.readTree(schemasDir.resolve("v3.2/as-code.schema.json").toFile());
        for (final String field : MIN_MAX_FIELDS) {
            final String description = root.at(field + "/description").asText();
            assertTrue(field + ": " + description, description.toLowerCase().contains("must be greater than or equal to `min"));
        }
    }

    @Test
    public void rulesCheckedOnlyByTheLoaderAreStatedInTheFieldDescriptions() throws IOException {
        final JsonNode root = JSON_MAPPER.readTree(schemasDir.resolve("v3.2/as-code.schema.json").toFile());
        final String startDate = root.at("/definitions/variables/date/allOf/1/properties/start_date/description").asText();
        assertTrue(startDate, startDate.contains("NeoLoad rejects a `start_date` that does not match `pattern`"));
        final String url = root.at("/definitions/user_paths/request_url/description").asText();
        assertTrue(url, url.contains("A variable is accepted in the host, the port and the path, not in the scheme"));
    }

    @Test
    public void everyObjectOfTheValidFixturesRejectsAnUnknownProperty() throws IOException, URISyntaxException {
        final List<String> accepted = new ArrayList<>();
        for (final Path file : fixturesValidatedBy32()) {
            final JsonNode node = read(file);
            if (!schema.validate(node).isEmpty()) {
                continue;
            }
            for (final String pointer : objectPointers(node, "")) {
                final JsonNode withUnknownKey = node.deepCopy();
                ((ObjectNode) withUnknownKey.at(pointer)).put(UNKNOWN_KEY, 1);
                if (schema.validate(withUnknownKey).isEmpty()) {
                    accepted.add(file.getFileName() + " " + (pointer.isEmpty() ? "/" : pointer));
                }
            }
        }
        assertTrue(accepted.size() + " object(s) accept the unknown key '" + UNKNOWN_KEY + "':\n" + String.join("\n", accepted),
                accepted.isEmpty());
    }

    private Set<ValidationMessage> validate(final String fixture) throws IOException {
        return schema.validate(YAML_MAPPER.readTree(getClass().getClassLoader().getResource(fixture + ".yaml")));
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

    /**
     * The YAML and JSON fixtures validated by schema 3.2: the ones not excluded and declaring no other schema version.
     */
    private List<Path> fixturesValidatedBy32() throws IOException, URISyntaxException {
        final URL anchor = getClass().getClassLoader().getResource("test-descriptor-only-required.yaml");
        Assert.assertNotNull(anchor);
        final Path resourceDir = Paths.get(anchor.toURI()).getParent();
        final List<Path> fixtures = new ArrayList<>();
        try (Stream<Path> files = Files.list(resourceDir)) {
            for (final Path file : files.filter(IOSchema3Dot2ValidationTest::isFixture).sorted().collect(Collectors.toList())) {
                if (!IOSchema3Dot1ValidationTest.declaresAnotherSchemaVersion(read(file), SCHEMA_VERSION)) {
                    fixtures.add(file);
                }
            }
        }
        return fixtures;
    }

    private static boolean isFixture(final Path file) {
        final String name = file.getFileName().toString();
        return Files.isRegularFile(file)
                && (name.endsWith(".yaml") || name.endsWith(".json"))
                && !IOSchema3Dot1ValidationTest.EXCLUDED_FIXTURES.contains(name);
    }

    private static JsonNode read(final Path file) throws IOException {
        final ObjectMapper mapper = file.getFileName().toString().endsWith(".yaml") ? YAML_MAPPER : JSON_MAPPER;
        return mapper.readTree(file.toFile());
    }

    /**
     * The JSON pointers of the given node, when it is an object, and of every object nested in it.
     */
    private static List<String> objectPointers(final JsonNode node, final String pointer) {
        final List<String> pointers = new ArrayList<>();
        if (node.isObject()) {
            pointers.add(pointer);
            node.fields().forEachRemaining(field -> pointers.addAll(
                    objectPointers(field.getValue(), pointer + "/" + field.getKey().replace("~", "~0").replace("/", "~1"))));
        } else if (node.isArray()) {
            for (int index = 0; index < node.size(); index++) {
                pointers.addAll(objectPointers(node.get(index), pointer + "/" + index));
            }
        }
        return pointers;
    }

    private static JsonSchema schema31() {
        return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(schemasDir.resolve("v3.1/as-code.schema.json").toUri());
    }
}
