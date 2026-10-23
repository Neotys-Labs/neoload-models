package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validator;
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
            "test-waituntil-invalid-timeout.yaml",
            "test-pacing-malformed-units.yaml",
            "test-pacing-mixed.yaml",
            "test-pacing-on-loop.yaml",
            "test-pacing-on-while.yaml",
            "test-date-variable-invalid-change-step.yaml",
            "test-assert-duration-unknown-property.yaml",
            "test-assertions-mutually-exclusive.yaml",
            "test-web_page-empty-steps.yaml",
            "test-web_page-execute-resources-invalid.yaml",
            "test-web_page-no-steps.yaml",
            "test-web_page-playback-invalid.yaml",
            "test-web_page-think-time-invalid-value.yaml",
            "test-web_page-think-time-min-only.yaml",
            "test-web_page-think-time-mixed.yaml",
            "test-web_page-think-time-with-name.yaml",
            "test-web_page-unsupported-step.yaml",
            "test-shared-elements-unsupported-type.yaml",
            "test-shared-elements-missing-name-loop.yaml",
            "test-shared-elements-missing-name-while.yaml",
            "test-shared-elements-missing-name-fork.yaml",
            "test-shared-elements-rejected-below-3-1.yaml",
            "test-shared-element-reference-step-rejected-below-3-1.yaml",
            "test-empty-collections.yaml",
            "test-empty-collections.json"
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

    @Test
    public void webPageThinkTimeSharesStepDurationSyntax() throws IOException {
        assertWebPageThinkTime("1m 30s", true);
        assertWebPageThinkTime("{value: 2000}", true);
        assertWebPageThinkTime("{max: 5s}", true);
        assertWebPageThinkTime("{min: 1s, max: 5s}", true);
        assertWebPageThinkTime("{name: tt, value: 2s}", false);
        assertWebPageThinkTime("{value: 2.5}", false);
    }

    @Test
    public void pacingAcceptsValueFormAndSpaces() throws IOException {
        assertPacing("{ value: 2s }", true);
        assertPacing("1h 1s", true);
        assertPacing("1500", true);
        assertPacing("{ max: 5s }", true);
        assertPacing("{ value: 2s, min: 1s }", false);
        assertPacing("abc", false);
        assertPacing("1 h", false);
        assertPacing("1m1h", false);
        assertPacing("1s1s", false);
        assertPacing("1s5", false);
        assertPacing("\" 1h 2m 5s \"", true);
        assertPacing("{ value: 5x }", false);
        assertPacing("{ min: abc, max: 5s }", false);
    }

    @Test
    public void durationVariableFollowsElementNameRules() throws IOException {
        assertPacing("${my-var}", true);
        assertPacing("${file.column}", true);
        assertPacing("${a€b}", false);
        assertPacing("${a$b}", false);
        assertPacing("${a.}", false);
    }

    @Test
    public void durationActionsRejectMalformedDurations() throws IOException {
        for (final String key : new String[] {"delay", "think_time"}) {
            assertStep(key + ": 1h 1s", true);
            assertStep(key + ": 1h 1m 1s 1ms", true);
            assertStep(key + ": 1h1m1s1ms", true);
            assertStep(key + ": ${my_var}", true);
            assertStep(key + ": { name: wait, max: 3000 }", true);
            assertStep(key + ": abc", false);
            assertStep(key + ": { name: wait, value: 1.5s }", false);
            assertStep(key + ": { name: wait, value: 2s, min: 1s }", false);
            assertStep(key + ": { value: 2s, max: 3s }", false);
            assertStep(key + ": { name: wait, other: 1s, max: 3s }", false);
        }
    }

    private static void assertStep(final String step, final boolean valid) throws IOException {
        final String yaml = "name: P\nuser_paths:\n- name: U\n  actions:\n    steps:\n    - " + step + "\n";
        Assert.assertEquals(step, valid, SCHEMA.validate(YAML_MAPPER.readTree(yaml)).isEmpty());
    }

    private static void assertPacing(final String pacing, final boolean valid) throws IOException {
        final String yaml = "name: P\nuser_paths:\n- name: U\n  actions:\n    pacing: " + pacing + "\n    steps:\n    - delay: 1s\n";
        Assert.assertEquals(pacing, valid, SCHEMA.validate(YAML_MAPPER.readTree(yaml)).isEmpty());
    }

    private static void assertWebPageThinkTime(final String thinkTime, final boolean valid) throws IOException {
        final String yaml = "name: P\nuser_paths:\n- name: U\n  actions:\n    steps:\n    - web_page:\n"
                + "        think_time: " + thinkTime + "\n        steps:\n        - request:\n            url: http://host:80/\n";
        Assert.assertEquals(thinkTime, valid, SCHEMA.validate(YAML_MAPPER.readTree(yaml)).isEmpty());
    }

    @Test
    public void schemaAndJavaModelAcceptAndRejectTheSameDurationValues() throws IOException {
        final String[] values = {"90", "90s", "1h30m", "120m", "500ms", "1s1h", "abc", "5x", "-5", "${v}"};
        final List<String> mismatches = new ArrayList<>();

        for (final DurationFields field : DurationFields.values()) {
            for (final String value : values) {
                final String yaml = field.yaml(value);
                final boolean schemaValid = SCHEMA.validate(YAML_MAPPER.readTree(yaml)).isEmpty();
                if (schemaValid != isValidForJava(yaml)) {
                    mismatches.add(field + "=" + value + " (schema " + (schemaValid ? "valid" : "invalid") + ")");
                }
            }
        }

        Assert.assertTrue("Schema and Java verdicts differ for: " + mismatches, mismatches.isEmpty());
    }

    private static boolean isValidForJava(final String yaml) {
        try {
            return new Validator().validate(new IO().read(yaml), NeoLoad.class).isValid();
        } catch (final IOException e) {
            return false;
        }
    }
}
