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
 * {@code soap_request} is a v3.1 step: it must be rejected by the v3.0 schema (not a recognized
 * step there) and accepted by the v3.1 schema.
 */
public class IOSoapRequestSchemaValidationTest {

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();

    private static JsonSchema schema30;
    private static JsonSchema schema31;
    private static JsonNode soapRequestDocument;

    @BeforeClass
    public static void loadSchemas() throws IOException, URISyntaxException {
        final Path schemasDir = locateSchemasDir();
        final Path schema30Path = schemasDir.resolve("v3.0/as-code.schema.json");
        final Path schema31Path = schemasDir.resolve("v3.1/as-code.schema.json");
        schema30 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schema30Path.toUri());
        schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schema31Path.toUri());

        soapRequestDocument = YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - soap_request:\n"
                        + "        url: http://host:80/\n"
                        + "        content:\n"
                        + "          path: ./requests/mySOAPRequest.xml\n");
    }

    @Test
    public void soapRequestRejectedBySchema3Dot0() {
        assertFalse("3.0 schema must not recognize soap_request", schema30.validate(soapRequestDocument).isEmpty());
    }

    @Test
    public void soapRequestAcceptedBySchema3Dot1() {
        assertTrue("3.1 schema must accept a valid soap_request", schema31.validate(soapRequestDocument).isEmpty());
    }

    @Test
    public void soapRequestWithValidParametersAcceptedBySchema3Dot1() throws IOException {
        final JsonNode document = YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - soap_request:\n"
                        + "        url: http://host:80/\n"
                        + "        parameters:\n"
                        + "        - param1: value1\n"
                        + "        content:\n"
                        + "          path: ./requests/mySOAPRequest.xml\n");
        assertTrue("3.1 schema must accept a valid parameters entry", schema31.validate(document).isEmpty());
    }

    @Test
    public void soapRequestWithNonStringParameterValueRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - soap_request:\n"
                        + "        url: http://host:80/\n"
                        + "        parameters:\n"
                        + "        - param1: 123\n"
                        + "        content:\n"
                        + "          path: ./requests/mySOAPRequest.xml\n");
        final Set<ValidationMessage> errors = schema31.validate(document);
        assertFalse("3.1 schema must reject a non-string parameters value", errors.isEmpty());
        assertTrue("error must be reported on the parameters field",
                errors.stream().anyMatch(error -> error.getInstanceLocation().toString().contains("parameters")));
    }

    @Test
    public void soapRequestWithValidHeadersAcceptedBySchema3Dot1() throws IOException {
        final JsonNode document = YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - soap_request:\n"
                        + "        url: http://host:80/\n"
                        + "        headers:\n"
                        + "        - SOAPAction: http://example.com/action\n"
                        + "        - Content-Type: text/xml; charset=utf-8\n"
                        + "        content:\n"
                        + "          path: ./requests/mySOAPRequest.xml\n");
        assertTrue("3.1 schema must accept valid headers entries", schema31.validate(document).isEmpty());
    }

    @Test
    public void soapRequestWithNonStringHeaderValueRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = YAML_MAPPER.readTree(
                "name: MyProject\n"
                        + "user_paths:\n"
                        + "- name: MyUserPath\n"
                        + "  actions:\n"
                        + "    steps:\n"
                        + "    - soap_request:\n"
                        + "        url: http://host:80/\n"
                        + "        headers:\n"
                        + "        - SOAPAction: 123\n"
                        + "        content:\n"
                        + "          path: ./requests/mySOAPRequest.xml\n");
        final Set<ValidationMessage> errors = schema31.validate(document);
        assertFalse("3.1 schema must reject a non-string headers value", errors.isEmpty());
        assertTrue("error must be reported on the headers field",
                errors.stream().anyMatch(error -> error.getInstanceLocation().toString().contains("headers")));
    }

    private static Path locateSchemasDir() throws URISyntaxException {
        final URL testClassesUrl = IOSoapRequestSchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        return moduleDir.resolve("../schemas").normalize();
    }
}
