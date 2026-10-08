package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Schema-level coverage for the settings of {@code websocket_request} that depend on each other,
 * so that an editor validating against the published 3.1 schema reports the same mistakes as
 * the model: each request of the invalid-settings fixture, which the model rejects, must also be
 * rejected by the schema on its own.
 */
public class IOWebSocketRequestSchemaValidationTest {
	private static final String INVALID_SETTINGS_FIXTURE = "test-websocket_request-invalid-settings.yaml";
	private static final ObjectMapper YAML_MAPPER = new YAMLMapper();
	private static JsonSchema schema31;

	@BeforeClass
	public static void loadSchema() throws URISyntaxException {
		final Path schema31Path = locateSchemasDir().resolve("v3.1/as-code.schema.json");
		schema31 = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schema31Path.toUri());
	}

	@Test
	public void everyInvalidSettingIsRejectedBySchema() throws IOException, URISyntaxException {
		final JsonNode steps = readFixture(INVALID_SETTINGS_FIXTURE).at("/user_paths/0/actions/steps");
		final JsonNode channel = steps.get(0);
		int requests = 0;
		for (final JsonNode step : steps) {
			if (!step.has("websocket_request")) {
				continue;
			}
			requests++;
			final String name = step.get("websocket_request").get("name").asText();
			assertFalse("the 3.1 schema must reject websocket_request '" + name + "'",
					schema31.validate(projectWith(channel, step)).isEmpty());
		}
		assertEquals("one request per setting rule", 7, requests);
	}

	@Test
	public void validRequestFixturesAreAccepted() throws IOException, URISyntaxException {
		assertValid(readFixture("test-websocket_request-minimal.yaml"));
		assertValid(readFixture("test-websocket_request-required-and-optional.yaml"));
		assertValid(readFixture("test-websocket_request-close-and-binary.yaml"));
		assertValid(readFixture("test-websocket_request-channel-paths.yaml"));
	}

	@Test
	public void explicitDefaultsAreAccepted() throws IOException {
		// The model accepts a setting written with its default value, so the schema must too
		assertValid(YAML_MAPPER.readTree(
				"name: MyProject\n"
						+ "user_paths:\n"
						+ "- name: MyUserPath\n"
						+ "  actions:\n"
						+ "    steps:\n"
						+ "    - websocket_channel:\n"
						+ "        name: my_channel\n"
						+ "        url: https://host:443/socket\n"
						+ "    - websocket_request:\n"
						+ "        channel: actions>my_channel\n"
						+ "        message_type: text\n"
						+ "        synchronous: false\n"
						+ "        status_code: 1000\n"
						+ "    - websocket_request:\n"
						+ "        channel: actions>my_channel\n"
						+ "        message_type: close\n"
						+ "        synchronous: false\n"
						+ "        status_code: \"1000\"\n"));
	}

	@Test
	public void channelPathPatternAcceptsUserPathAndSharedElementRoots() throws IOException {
		assertValid(projectWithChannelReference("actions>my_channel"));
		assertValid(projectWithChannelReference("shared_elements>OpenChat>chat_socket"));
		assertValid(projectWithChannelReference("shared_elements>OpenChat>Login>chat_socket"));
		for (final String reference : new String[] {"my_channel", "shared_elements>OpenChat", "Listener>init>my_channel", "actions>", "actions>>my_channel"}) {
			assertFalse("the 3.1 schema must reject channel '" + reference + "'",
					schema31.validate(projectWithChannelReference(reference)).isEmpty());
		}
	}

	private static JsonNode projectWithChannelReference(final String reference) throws IOException {
		final JsonNode request = YAML_MAPPER.readTree("websocket_request:\n  body: hello\n");
		((ObjectNode) request.get("websocket_request")).put("channel", reference);
		return projectWith(YAML_MAPPER.readTree("delay: 1s\n"), request);
	}

	private static JsonNode projectWith(final JsonNode channel, final JsonNode request) {
		final ObjectNode project = YAML_MAPPER.createObjectNode().put("name", "MyProject");
		final ObjectNode userPath = project.putArray("user_paths").addObject().put("name", "MyUserPath");
		userPath.putObject("actions").putArray("steps").add(channel).add(request);
		return project;
	}

	private static void assertValid(final JsonNode document) {
		final Set<ValidationMessage> messages = schema31.validate(document);
		assertTrue("unexpected schema errors: " + messages, messages.isEmpty());
	}

	private static JsonNode readFixture(final String resourceName) throws IOException, URISyntaxException {
		final URL fixtureUrl = IOWebSocketRequestSchemaValidationTest.class.getClassLoader().getResource(resourceName);
		assertNotNull("Missing classpath resource " + resourceName, fixtureUrl);
		return YAML_MAPPER.readTree(new File(fixtureUrl.toURI()));
	}

	private static Path locateSchemasDir() throws URISyntaxException {
		final URL testClassesUrl = IOWebSocketRequestSchemaValidationTest.class.getProtectionDomain().getCodeSource().getLocation();
		final Path moduleDir = Paths.get(testClassesUrl.toURI()).getParent().getParent();
		return moduleDir.resolve("../schemas").normalize();
	}
}
