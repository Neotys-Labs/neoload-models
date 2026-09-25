package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static Path locateSchemasDir() throws URISyntaxException {
        final URL testClassesUrl = IOSoapRequestSchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        return moduleDir.resolve("../schemas").normalize();
    }
}
