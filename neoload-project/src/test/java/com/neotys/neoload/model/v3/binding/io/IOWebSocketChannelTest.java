package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Header;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.project.userpath.WebSocketChannel;
import com.neotys.neoload.model.v3.project.userpath.WebSocketMessagesMapping;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class IOWebSocketChannelTest extends AbstractIOElementsTest {

	private static final Validator VALIDATOR = new Validator();

	private static Step getWebSocketChannelOnlyRequired() {
		return WebSocketChannel.builder()
				.name("my_channel")
				.url("https://host:443/socket")
				.build();
	}

	private static Step getWebSocketChannelRequiredAndOptional() {
		return WebSocketChannel.builder()
				.name("full_channel")
				.description("a channel using every setting")
				.url("/socket")
				.server("myserver")
				.addHeaders(Header.builder().name("Origin").value("https://host").build())
				.addExtractors(VariableExtractor.builder()
						.name("session_id")
						.regexp("session=([0-9]+)")
						.build())
				.messagesMapping(WebSocketMessagesMapping.builder()
						.jsonPath("$.correlationId")
						.build())
				.build();
	}

	private static Step getWebSocketChannelMessagesMappingXpath() {
		return WebSocketChannel.builder()
				.name("xpath_channel")
				.url("http://host:80/socket")
				.messagesMapping(WebSocketMessagesMapping.builder()
						.xpath("/message/@id")
						.template("$2$")
						.decode(WebSocketMessagesMapping.Decode.CUSTOM)
						.customDecoder("com.example.MyDecoder")
						.encoding("UTF-8")
						.build())
				.build();
	}

	@Test
	public void readWebSocketChannelOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getWebSocketChannelOnlyRequired());
		assertNotNull(expectedProject);

		read("test-websocket_channel-only-required", expectedProject);
	}

	@Test
	public void readWebSocketChannelRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getWebSocketChannelRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-websocket_channel-required-and-optional", expectedProject);
	}

	@Test
	public void readWebSocketChannelMessagesMappingXpath() throws IOException {
		final Project expectedProject = buildProject(getWebSocketChannelMessagesMappingXpath());
		assertNotNull(expectedProject);

		read("test-websocket_channel-messages-mapping-xpath", expectedProject);
	}

	@Test
	public void writeWebSocketChannelOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getWebSocketChannelOnlyRequired());
		assertNotNull(expectedProject);

		write("test-websocket_channel-only-required", expectedProject);
	}

	@Test
	public void writeWebSocketChannelRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getWebSocketChannelRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-websocket_channel-required-and-optional", expectedProject);
	}

	@Test
	public void writeWebSocketChannelMessagesMappingXpath() throws IOException {
		final Project expectedProject = buildProject(getWebSocketChannelMessagesMappingXpath());
		assertNotNull(expectedProject);

		write("test-websocket_channel-messages-mapping-xpath", expectedProject);
	}

	@Test
	public void readWebSocketChannelNoUrlRejected() throws IOException {
		assertInvalid("test-websocket_channel-no-url", "url");
	}

	@Test
	public void readWebSocketChannelWithWsSchemesRejected() throws IOException {
		// NeoLoad stores a channel URL as http or https: ws and wss are not accepted
		final Validation validation = validate("test-websocket_channel-ws-schemes");
		assertFalse(validation.isValid());
		final String message = validation.getMessage().orElse("");
		assertTrue("unexpected message: " + message, message.startsWith("Data Model is invalid. Violation Number: 2."));
		assertTrue("unexpected message: " + message, message.contains("url"));
	}

	@Test
	public void readWebSocketChannelsWithSameNameInDifferentContainersAccepted() throws IOException {
		// A channel is designated by its complete path, so only the path has to be unique
		final Validation validation = validate("test-websocket_channel-same-name-different-paths");
		assertTrue("unexpected message: " + validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void readWebSocketChannelMessagesMappingXpathAndJsonPathRejected() throws IOException {
		assertInvalid("test-websocket_channel-messages-mapping-xpath-and-jsonpath",
				"xpath and jsonpath cannot be used simultaneously");
	}

	private Validation validate(final String fileName) throws IOException {
		final IO io = new IO();
		final File file = getFile(fileName, "yaml");
		final ProjectDescriptor descriptor = io.read(file);

		return VALIDATOR.validate(descriptor, NeoLoad.class);
	}

	private void assertInvalid(final String fileName, final String expectedInMessage) throws IOException {
		final Validation validation = validate(fileName);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().isPresent());
		assertTrue("unexpected message: " + validation.getMessage().get(),
				validation.getMessage().get().contains(expectedInMessage));
	}
}
