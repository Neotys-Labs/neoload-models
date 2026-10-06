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
 * {@code web_services_security} and the soap_request security profile references are v3.1 features:
 * the v3.1 schema accepts well-formed entries and rejects malformed ones, the v3.0 schema rejects them.
 */
public class IOWebServicesSecuritySchemaValidationTest {

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();

    private static final String SOAP_USER_PATH = ""
            + "user_paths:\n"
            + "- name: MyUserPath\n"
            + "  actions:\n"
            + "    steps:\n"
            + "    - soap_request:\n"
            + "        url: http://host:80/\n"
            + "        content:\n"
            + "          path: ./requests/mySOAPRequest.xml\n"
            + "        request_security_profile: MyRequestProfile\n"
            + "        response_security_profile: MyResponseProfile\n";

    private static JsonSchema schema30;
    private static JsonSchema schema31;

    @BeforeClass
    public static void loadSchemas() throws URISyntaxException {
        final Path schemasDir = locateSchemasDir();
        schema30 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemasDir.resolve("v3.0/as-code.schema.json").toUri());
        schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemasDir.resolve("v3.1/as-code.schema.json").toUri());
    }

    @Test
    public void webServicesSecurityAcceptedBySchema3Dot1AndRejectedBySchema3Dot0() throws IOException {
        final JsonNode document = project(requestProfile("digest", "username:\n            username: user\n            password: secret"));

        assertTrue("3.1 schema must accept web_services_security", schema31.validate(document).isEmpty());
        assertFalse("3.0 schema must reject web_services_security", schema30.validate(document).isEmpty());
    }

    @Test
    public void unknownPasswordTypeRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("plain", "username:\n            username: user\n            password: secret"));

        assertFalse(schema31.validate(document).isEmpty());
    }

    @Test
    public void usernameTokenWithoutPasswordAcceptedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("digest", "username:\n            username: user"));

        assertTrue(schema31.validate(document).isEmpty());
    }

    @Test
    public void decimalTimeToLiveRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("digest", "timestamp:\n            time_to_live: 300.5"));

        assertFalse(schema31.validate(document).isEmpty());
    }

    @Test
    public void derivedPasswordTypeAcceptedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("\n              derived_key:\n                salt: mysalt\n                iteration: 1000", "username:\n            username: user\n            password: secret"));

        assertTrue(schema31.validate(document).isEmpty());
    }

    @Test
    public void derivedPasswordTypeWithVariableIterationAcceptedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("\n              derived_key:\n                salt: mysalt\n                iteration: ${iterations}", "username:\n            username: user\n            password: secret"));

        assertTrue(schema31.validate(document).isEmpty());
    }

    @Test
    public void derivedPasswordTypeWithoutDerivedKeyRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("\n              salt: mysalt\n              iteration: 1000", "username:\n            username: user\n            password: secret"));

        assertFalse(schema31.validate(document).isEmpty());
    }

    @Test
    public void derivedPasswordTypeWithoutIterationRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("\n              derived_key:\n                salt: mysalt", "username:\n            username: user\n            password: secret"));

        assertFalse(schema31.validate(document).isEmpty());
    }

    @Test
    public void derivedPasswordTypeWithZeroIterationRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("\n              derived_key:\n                salt: mysalt\n                iteration: 0", "username:\n            username: user\n            password: secret"));

        assertFalse(schema31.validate(document).isEmpty());
    }

    @Test
    public void tokenWithBothUsernameAndTimestampRejectedBySchema3Dot1() throws IOException {
        final JsonNode document = project(requestProfile("digest", "username:\n            username: user\n        timestamp:\n            id: TS-1"));

        assertFalse(schema31.validate(document).isEmpty());
    }

    @Test
    public void unknownTokenKeyRejectedBySchema3Dot1()throws IOException {
        final JsonNode document = project(requestProfile("digest", "signature:\n            id: SIG-1"));

        assertFalse(schema31.validate(document).isEmpty());
    }

    private static String requestProfile(final String passwordType, final String token) {
        final String tokenWithPasswordType = token.startsWith("username")
                ? token + "\n            password_type: " + passwordType
                : token;
        return ""
                + "web_services_security:\n"
                + "  keystores:\n"
                + "  - path: ./wss-keystores/server.p12\n"
                + "  request_profiles:\n"
                + "  - name: MyRequestProfile\n"
                + "    headers:\n"
                + "    - actor: http://example.com/actor\n"
                + "      must_understand: true\n"
                + "      tokens:\n"
                + "      - " + tokenWithPasswordType + "\n"
                + "  response_profiles:\n"
                + "  - name: MyResponseProfile\n"
                + "    keystore: ./wss-keystores/server.p12\n";
    }

    private static JsonNode project(final String webServicesSecurity) throws IOException {
        return YAML_MAPPER.readTree("name: MyProject\n" + webServicesSecurity + SOAP_USER_PATH);
    }

    private static Path locateSchemasDir() throws URISyntaxException {
        final URL testClassesUrl = IOWebServicesSecuritySchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
        final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
        return moduleDir.resolve("../schemas").normalize();
    }
}
