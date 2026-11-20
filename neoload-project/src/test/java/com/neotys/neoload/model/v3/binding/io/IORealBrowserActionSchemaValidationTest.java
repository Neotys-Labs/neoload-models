package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A Real Browser step ({@code browser_click}...) is a v3.1 step: it only accepts the parameter names of its
 * action and requires the mandatory ones. A {@code custom_action} keeps accepting free parameters, whatever its type.
 */
public class IORealBrowserActionSchemaValidationTest {

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();

    private static JsonSchema schema30;
    private static JsonSchema schema31;

    @BeforeClass
    public static void loadSchemas() throws URISyntaxException {
        final Path schemasDir = locateSchemasDir();
        schema30 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemasDir.resolve("v3.0/as-code.schema.json").toUri());
        schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemasDir.resolve("v3.1/as-code.schema.json").toUri());
    }

    @Test
    public void realBrowserJourneyAccepted() throws IOException {
        assertValid(schema31, steps(
                "    - browser_open:\n"
                        + "        name: Open browser\n"
                        + "        parameters:\n"
                        + "        - { name: browser, value: chromium }\n"
                        + "        - { name: headless, value: \"true\" }\n"
                        + "        - { name: header, value: \"X-Test=1\" }\n"
                        + "        - { name: header, value: \"(tricentis.com) X-Other=2\" }\n"
                        + "        - { name: httpCredentials.login, value: admin }\n"
                        + "        - { name: httpCredentials.password, value: secret, type: PASSWORD }\n"
                        + "        - { name: httpCredentials.origin, value: \"https://tricentis.com\" }\n"
                        + "    - browser_capture_requests:\n"
                        + "        name: Capture requests\n"
                        + "        parameters:\n"
                        + "        - { name: resourceType, value: image }\n"
                        + "        - { name: resourceType, value: script }\n"
                        + "    - browser_navigate:\n"
                        + "        name: Go to home\n"
                        + "        asRequest: true\n"
                        + "        parameters:\n"
                        + "        - { name: url, value: \"https://www.tricentis.com/\" }\n"
                        + "        - { name: waitForNavigation.until, value: NETWORKIDLE }\n"
                        + "        duration_assertion:\n"
                        + "          less_than: 5000\n"
                        + "    - browser_click:\n"
                        + "        name: Click login\n"
                        + "        parameters:\n"
                        + "        - { name: selector, value: 'role=link[name=\"Log in\"]' }\n"
                        + "        - { name: selector, value: '#login' }\n"
                        + "        - { name: timeout, value: \"8000\" }\n"
                        + "        - { name: frameName, value: main }\n"
                        + "        - { name: waitForDownload.path, value: /tmp/report.pdf }\n"
                        + "    - browser_type:\n"
                        + "        name: Type password\n"
                        + "        parameters:\n"
                        + "        - { name: selector, value: '#password' }\n"
                        + "        - { name: encryptedText, value: secret, type: PASSWORD }\n"
                        + "    - browser_select_option:\n"
                        + "        name: Select countries\n"
                        + "        parameters:\n"
                        + "        - { name: selector, value: '#country' }\n"
                        + "        - { name: 'options[0].text', value: France }\n"
                        + "        - { name: 'options[12].value', value: DE }\n"
                        + "    - browser_upload_file:\n"
                        + "        name: Upload files\n"
                        + "        parameters:\n"
                        + "        - { name: selector, value: 'input[type=file]' }\n"
                        + "        - { name: filePaths, value: '${NL-CustomResources}/a.txt;${NL-CustomResources}/b.txt' }\n"
                        + "    - browser_open_tab:\n"
                        + "        name: Open tab\n"
                        + "    - browser_close:\n"
                        + "        name: Close browser\n"));
    }

    @Test
    public void nameIsOptionalAndBareStepAccepted() throws IOException {
        assertValid(schema31, steps(
                "    - browser_open:\n"
                        + "        parameters:\n"
                        + "        - { name: browser, value: chromium }\n"
                        + "    - browser_open_tab\n"
                        + "    - browser_close\n"));
    }

    @Test
    public void bareStepWithRequiredParameterRejected() throws IOException {
        assertInvalid(schema31, steps("    - browser_navigate\n"));
    }

    @Test
    public void realBrowserStepRejectedBySchema3Dot0() throws IOException {
        assertInvalid(schema30, steps(
                "    - browser_open_tab:\n"
                        + "        name: Open tab\n"));
    }

    @Test
    public void unknownParameterNameRejected() throws IOException {
        assertInvalid(schema31, steps(
                "    - browser_click:\n"
                        + "        name: Click login\n"
                        + "        parameters:\n"
                        + "        - { name: selectr, value: '#login' }\n"));
    }

    @Test
    public void parameterOfAnotherActionRejected() throws IOException {
        assertInvalid(schema31, steps(
                "    - browser_navigate:\n"
                        + "        name: Go to home\n"
                        + "        parameters:\n"
                        + "        - { name: url, value: \"https://www.tricentis.com/\" }\n"
                        + "        - { name: selector, value: '#login' }\n"));
    }

    @Test
    public void missingRequiredParameterRejected() throws IOException {
        assertInvalid(schema31, steps(
                "    - browser_upload_file:\n"
                        + "        name: Upload files\n"
                        + "        parameters:\n"
                        + "        - { name: selector, value: 'input[type=file]' }\n"));
    }

    @Test
    public void missingParametersOnActionWithRequiredParameterRejected() throws IOException {
        assertInvalid(schema31, steps(
                "    - browser_navigate:\n"
                        + "        name: Go to home\n"));
    }

    @Test
    public void parameterOnActionWithoutParameterRejected() throws IOException {
        assertInvalid(schema31, steps(
                "    - browser_open_tab:\n"
                        + "        name: Open tab\n"
                        + "        parameters:\n"
                        + "        - { name: index, value: \"1\" }\n"));
    }

    @Test
    public void typeOnRealBrowserStepRejected() throws IOException {
        assertInvalid(schema31, steps(
                "    - browser_click:\n"
                        + "        name: Click login\n"
                        + "        type: BrowserClick\n"
                        + "        parameters:\n"
                        + "        - { name: selector, value: '#login' }\n"));
    }

    @Test
    public void customActionKeepsFreeParameters() throws IOException {
        assertValid(schema31, steps(
                "    - custom_action:\n"
                        + "        name: My Java action\n"
                        + "        type: MyCustomAdvancedAction\n"
                        + "        libraryPath: lib/my-action.jar\n"
                        + "        parameters:\n"
                        + "        - { name: selectr, value: anything }\n"
                        + "        - { name: whatever.I.want, value: \"42\" }\n"
                        + "    - custom_action:\n"
                        + "        name: Click login\n"
                        + "        type: BrowserClick\n"
                        + "        parameters:\n"
                        + "        - { name: selectr, value: '#login' }\n"));
    }

    private static JsonNode steps(final String steps) throws IOException {
        return YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + steps);
    }

    private static void assertValid(final JsonSchema schema, final JsonNode document) {
        final Set<ValidationMessage> errors = schema.validate(document);
        assertTrue("schema must accept the document: " + errors, errors.isEmpty());
    }

    private static void assertInvalid(final JsonSchema schema, final JsonNode document) {
        assertFalse("schema must reject the document", schema.validate(document).isEmpty());
    }

    private static Path locateSchemasDir() throws URISyntaxException {
        final URL testClassesUrl = IORealBrowserActionSchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        return moduleDir.resolve("../schemas").normalize();
    }
}
