package com.neotys.neoload.model.v3.binding.io;

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
 * Validates every YAML and JSON fixture in {@code neoload-project/src/test/resources} against the canonical
 * as-code v3.1 JSON Schema ({@code schemas/v3.1/as-code.schema.json}).
 *
 * <p>A failure means one of two things:
 * <ul>
 *   <li>The schema is missing a keyword that the Java model serialises (e.g. a new field was
 *       added to a variable type but not declared in the schema).</li>
 *   <li>The fixture uses syntax that is not yet described in the schema.</li>
 * </ul>
 *
 * <p>Files listed in {@link #EXCLUDED_FIXTURES} are excluded because they intentionally
 * exercise old input formats the Java parser accepts for backward compatibility, but which do
 * not conform to the current schema (e.g. SLA thresholds without the required
 * {@code "per test/interval"} suffix), or they are voluntarily invalid and used in some unit-test...
 */
public class IOSchema3Dot1ValidationTest {

    private static JsonSchema SCHEMA;
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * Fixtures excluded from schema validation.
     *
     * <p><b>Backward-compat fixtures</b> ({@code test-readonly-*}): use legacy input formats the
     * Java parser accepts but that do not conform to the current schema (e.g. SLA thresholds
     * without the required "per test/interval" suffix).</p>
     *
     * <p><b>Encoding-specific fixtures</b>: exercise non-UTF-8 encoding handling; their content is
     * valid but cannot be parsed by the standard UTF-8 Jackson mapper used here.</p>
     *
     * <p><b>Voluntarily invalid files for unit-test</b>: used for some other unit testing.</p>
     *
     * <p>Do not add new fixtures here without a clear justification.</p>
     */
    private static final Set<String> EXCLUDED_FIXTURES = Set.of(
            // Backward-compat: SLA thresholds without "per test/interval"
            "test-readonly-slaprofiles-only-required.yaml",
            "test-readonly-slaprofiles-only-required.json",
            "test-readonly-slaprofiles-required-and-optional.yaml",
            "test-readonly-slaprofiles-required-and-optional.json",
            // Backward-compat: while conditions using old-style ANTLR-only keywords
            "test-readonly-while-only-required.yaml",
            "test-readonly-while-only-required.json",
            "test-readonly-while-required-and-optional.yaml",
            "test-readonly-while-required-and-optional.json",
            // Encoding-specific: ISO-8859-1 encoded; tests charset handling, not schema conformance
            "test-scenarios-iso-8859-1.yaml",
            "test-scenarios-iso-8859-1.json",
            // Voluntarily invalid files for unit-test:
            "test-try-catch-invalid-caught-exceptions.yaml",
            "test-current-date-variable-offsets-invalid.yaml",
            "test-pacing-invalid-values.yaml",
            "test-pacing-mixed.yaml",
            "test-pacing-on-loop.yaml",
            "test-pacing-on-while.yaml"
    );

    @BeforeClass
    public static void loadSchema() throws URISyntaxException {
        // Resolve schema relative to the module root: target/test-classes -> target -> neoload-project -> ../schemas/
        URL testClassesUrl = IOSchema3Dot1ValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        Path schemaPath = moduleDir.resolve("../schemas/v3.1/as-code.schema.json").normalize();
        SCHEMA = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemaPath.toUri());
    }

    @Test
    public void allFixturesConformToSchema() throws IOException, URISyntaxException {
        URL anchor = getClass().getClassLoader().getResource("test-descriptor-only-required.yaml");
        Assert.assertNotNull(anchor);
        Path resourceDir = Paths.get(anchor.toURI()).getParent();

        List<String> failures = new ArrayList<>();

        try (Stream<Path> files = Files.list(resourceDir)) {
            files.filter(Files::isRegularFile)
                 .filter(p -> {
                     String name = p.getFileName().toString();
                     return name.endsWith(".yaml") || name.endsWith(".json");
                 })
                 .filter(p -> !EXCLUDED_FIXTURES.contains(p.getFileName().toString()))
                 .sorted()
                 .forEach(file -> {
                     String fileName = file.getFileName().toString();
                     try {
                         ObjectMapper mapper = fileName.endsWith(".yaml") ? YAML_MAPPER : JSON_MAPPER;
                         JsonNode node = mapper.readTree(file.toFile());
                         Set<ValidationMessage> errors = SCHEMA.validate(node);
                         if (!errors.isEmpty()) {
                             String details = errors.stream()
                                     .map(ValidationMessage::getMessage)
                                     .sorted()
                                     .collect(Collectors.joining("\n  "));
                             failures.add(fileName + ":\n  " + details);
                         }
                     } catch (IOException e) {
                         failures.add(fileName + ": parse error: " + e.getMessage());
                     }
                 });
        }

        if (!failures.isEmpty()) {
            fail(failures.size() + " fixture(s) failed schema validation:\n\n"
                    + String.join("\n\n", failures));
        }
    }

    private Set<ValidationMessage> validate(final String fixture) throws IOException {
        final File file = new File(getClass().getClassLoader().getResource(fixture + ".yaml").getFile());
        final JsonNode node = YAML_MAPPER.readTree(file);
        return SCHEMA.validate(node);
    }

    @Test
    public void pacingOnLoopFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected 'pacing' on a loop step to fail schema validation",
                validate("test-pacing-on-loop").isEmpty());
    }

    @Test
    public void pacingOnWhileFailsSchemaValidation() throws IOException {
        Assert.assertFalse("Expected 'pacing' on a while step to fail schema validation",
                validate("test-pacing-on-while").isEmpty());
    }
}
