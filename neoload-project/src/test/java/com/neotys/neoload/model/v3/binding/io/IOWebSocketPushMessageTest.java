package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Match;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.project.userpath.WebSocketChannel;
import com.neotys.neoload.model.v3.project.userpath.WebSocketPushMessage;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Test;

public class IOWebSocketPushMessageTest extends AbstractIOElementsTest {

	private static final Validator VALIDATOR = new Validator();
	private static final String MESSAGE_CONTENT = "${NL-MessageContent}";
	private static final String CHANNEL = "actions>chat_socket";

	private static Condition contains(final String value) {
		return Condition.builder()
				.operand1(MESSAGE_CONTENT)
				.operator(Condition.Operator.CONTAINS)
				.operand2(value)
				.build();
	}

	private static Project getPushMessagesRequiredAndOptional() {
		return buildProject(
				WebSocketChannel.builder()
						.name("chat_socket")
						.url("wss://host:443/chat")
						.addPushMessages(WebSocketPushMessage.builder()
								.name("on_ping")
								.conditions(List.of(contains("ping")))
								.addSteps(WebSocketRequest.builder()
										.channel(CHANNEL)
										.body("pong")
										.build())
								.build())
						.addPushMessages(WebSocketPushMessage.builder()
								.name("on_order_update")
								.description("an order status pushed by the server")
								.conditions(List.of(contains("order"), contains("status")))
								.match(Match.ALL)
								.charset("UTF-8")
								.addExtractors(VariableExtractor.builder()
										.name("order_id")
										.jsonPath("$.order.id")
										.build())
								.addAssertions(ContentAssertion.builder()
										.contains("status")
										.build())
								.addSteps(Container.builder()
										.name("Order")
										.addSteps(Request.builder()
												.url("https://host/orders/${order_id}")
												.build())
										.build())
								.build())
						// No steps: it only keeps these frames away from the fallback
						.addPushMessages(WebSocketPushMessage.builder()
								.name("on_heartbeat")
								.conditions(List.of(Condition.builder()
										.operand1(MESSAGE_CONTENT)
										.operator(Condition.Operator.EQUALS)
										.operand2("hb")
										.build()))
								.build())
						// No conditions: the fallback
						.addPushMessages(WebSocketPushMessage.builder()
								.name("anything_else")
								.addSteps(WebSocketRequest.builder()
										.channel(CHANNEL)
										.body("unknown message")
										.build())
								.build())
						.build());
	}

	@Test
	public void readPushMessagesRequiredAndOptional() throws IOException {
		final Project expectedProject = getPushMessagesRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-websocket_push_message-required-and-optional", expectedProject);
	}

	@Test
	public void writePushMessagesRequiredAndOptional() throws IOException {
		final Project expectedProject = getPushMessagesRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-websocket_push_message-required-and-optional", expectedProject);
	}

	@Test
	public void readChannelPathsThroughPushMessagesAccepted() throws IOException {
		// Requests inside push messages, channels declared inside push messages, referenced from
		// inside and from outside them, in a user path and in a shared element
		final Validation validation = validate("test-websocket_push_message-channel-paths");
		assertTrue("unexpected message: " + validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void readInvalidPushMessagesRejected() throws IOException {
		// valid_fallback, without conditions nor match, must not be reported
		assertInvalid("test-websocket_push_message-invalid",
				"push message 'empty_conditions': 'conditions' must hold at least one condition; a push message without 'conditions' is the fallback.",
				"push message 'match_without_conditions': 'match' is only used with 'conditions'",
				"push message 'default_match_without_conditions': 'match' is only used with 'conditions'");
	}

	@Test
	public void readInvalidReferencesInsidePushMessagesRejected() throws IOException {
		assertInvalid("test-websocket_push_message-invalid-references",
				"websocket_request 'wrong_inside': no websocket_channel at 'actions>on_ping>chat_socket'; use 'actions>chat_socket'.",
				"websocket_request 'sync_without_mapping': a synchronous request needs its channel 'actions>chat_socket' to declare 'messages_mapping'.",
				"websocket_request 'through_twins': 'actions>chat_socket>twin>inner' designates 2 channels: two sibling steps on this path share a name.",
				"no websocket_channel at 'shared_elements>OpenSupport>on_agent>support_socket'; use 'shared_elements>OpenSupport>support_socket'.");
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
