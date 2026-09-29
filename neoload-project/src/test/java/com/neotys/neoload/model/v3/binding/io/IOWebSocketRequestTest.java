package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.project.userpath.WebSocketChannel;
import com.neotys.neoload.model.v3.project.userpath.WebSocketMessagesMapping;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class IOWebSocketRequestTest extends AbstractIOElementsTest {

	private static final Validator VALIDATOR = new Validator();

	private static Project getWebSocketRequestMinimal() {
		return buildProject(
				WebSocketChannel.builder()
						.name("my_channel")
						.url("wss://host:443/socket")
						.build(),
				WebSocketRequest.builder()
						.channel("actions>my_channel")
						.body("hello")
						.build());
	}

	private static Project getWebSocketRequestRequiredAndOptional() {
		return buildProject(
				WebSocketChannel.builder()
						.name("chat_socket")
						.url("wss://host:443/chat")
						.messagesMapping(WebSocketMessagesMapping.builder()
								.jsonPath("$.correlationId")
								.build())
						.build(),
				WebSocketRequest.builder()
						.name("send_hello")
						.description("a synchronous request using every setting")
						.channel("actions>chat_socket")
						.isSynchronous(true)
						.mappingId("444-555-1234")
						.body("hello")
						.addExtractors(VariableExtractor.builder()
								.name("session_id")
								.regexp("session=([0-9]+)")
								.build())
						.addAssertions(ContentAssertion.builder()
								.contains("welcome")
								.build())
						.build());
	}

	private static Project getWebSocketRequestCloseAndBinary() {
		return buildProject(
				WebSocketChannel.builder()
						.name("my_channel")
						.url("wss://host:443/socket")
						.build(),
				WebSocketRequest.builder()
						.name("send_binary")
						.channel("actions>my_channel")
						.messageType(WebSocketRequest.MessageType.BINARY)
						.bodyBinary(new byte[] {0, 1, 2, (byte) 0xFF, (byte) 0xFE})
						.build(),
				WebSocketRequest.builder()
						.name("close_channel")
						.channel("actions>my_channel")
						.messageType(WebSocketRequest.MessageType.CLOSE)
						.statusCode("1001")
						.body("going away")
						.build());
	}

	@Test
	public void readWebSocketRequestMinimal() throws IOException {
		read("test-websocket_request-minimal", getWebSocketRequestMinimal());
	}

	@Test
	public void writeWebSocketRequestMinimal() throws IOException {
		write("test-websocket_request-minimal", getWebSocketRequestMinimal());
	}

	@Test
	public void readWebSocketRequestRequiredAndOptional() throws IOException {
		read("test-websocket_request-required-and-optional", getWebSocketRequestRequiredAndOptional());
	}

	@Test
	public void writeWebSocketRequestRequiredAndOptional() throws IOException {
		write("test-websocket_request-required-and-optional", getWebSocketRequestRequiredAndOptional());
	}

	@Test
	public void readWebSocketRequestCloseAndBinary() throws IOException {
		read("test-websocket_request-close-and-binary", getWebSocketRequestCloseAndBinary());
	}

	@Test
	public void writeWebSocketRequestCloseAndBinary() throws IOException {
		write("test-websocket_request-close-and-binary", getWebSocketRequestCloseAndBinary());
	}

	@Test
	public void readChannelPathsThroughEveryKindOfStepAccepted() throws IOException {
		final Validation validation = validate("test-websocket_request-channel-paths");
		assertTrue("unexpected message: " + validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void readInvalidChannelReferencesRejected() throws IOException {
		assertInvalid("test-websocket_request-invalid-references",
				"websocket_request 'bare_name': 'chat_socket' is not a channel path: it must start with init, actions, end",
				"websocket_request 'wrong_path': no websocket_channel at 'actions>Login>chat_socket'; use 'actions>chat_socket'.",
				"websocket_request 'ambiguous': 'actions>Twin>twin_socket' designates 2 channels: two sibling steps on this path share a name.",
				"websocket_request 'shared_root': 'shared_elements>OpenChat>chat_socket' is not a channel path",
				// The reference is user text: it must reach the message as written, not be interpolated
				"websocket_request 'with_variable': no websocket_channel at 'actions>${channel}'.",
				"websocket_request 'no_messages_mapping': a synchronous request needs its channel 'actions>chat_socket' to declare 'messages_mapping'."
				);

	}

	@Test
	public void readInvalidRequestSettingsRejected() throws IOException {
		assertInvalid("test-websocket_request-invalid-settings",
				"websocket_request 'close_sync': a 'close' request cannot be synchronous.",
				"websocket_request 'sync_no_mapping_id': a synchronous request requires 'mapping_id'.",
				"websocket_request 'async_mapping_id': 'mapping_id' is only used by a synchronous request.",
				"websocket_request 'async_extractors': 'extractors' are only applied on a synchronous request",
				"websocket_request 'async_assertions': 'assertions' are only checked on a synchronous request",
				"websocket_request 'text_status_code': 'status_code' is only used by a 'close' request.",
				"websocket_request 'both_bodies': 'body' and 'bodybinary' cannot be used simultaneously.");
	}

	private Validation validate(final String fileName) throws IOException {
		final IO io = new IO();
		final File file = getFile(fileName, "yaml");
		final ProjectDescriptor descriptor = io.read(file, StandardCharsets.UTF_8);
		assertNotNull(descriptor);

		return VALIDATOR.validate(descriptor, NeoLoad.class);
	}

	private void assertInvalid(final String fileName, final String... expectedInMessage) throws IOException {
		final Validation validation = validate(fileName);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().isPresent());
		final String message = validation.getMessage().get();
		assertTrue("unexpected violation count in: " + message,
				message.startsWith("Data Model is invalid. Violation Number: " + expectedInMessage.length + "."));
		for (final String expected : expectedInMessage) {
			assertTrue("missing '" + expected + "' in: " + message, message.contains(expected));
		}
	}
}
