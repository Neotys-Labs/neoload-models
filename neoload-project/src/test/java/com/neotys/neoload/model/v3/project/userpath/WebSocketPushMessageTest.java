package com.neotys.neoload.model.v3.project.userpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Element;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class WebSocketPushMessageTest {
	@Test
	public void constants() {
		assertEquals("name", WebSocketPushMessage.NAME);
		assertEquals("description", WebSocketPushMessage.DESCRIPTION);
		assertEquals("conditions", WebSocketPushMessage.CONDITIONS);
		assertEquals("charset", WebSocketPushMessage.CHARSET);
		assertEquals("extractors", WebSocketPushMessage.EXTRACTORS);
		assertEquals("steps", WebSocketPushMessage.STEPS);
		assertEquals("push_messages", WebSocketChannel.PUSH_MESSAGES);
	}

	@Test
	public void defaults() {
		final WebSocketPushMessage pushMessage = WebSocketPushMessage.builder().build();
		assertEquals(WebSocketPushMessage.DEFAULT_NAME, pushMessage.getName());
		assertFalse(pushMessage.getConditions().isPresent());
		assertFalse(pushMessage.getMatch().isPresent());
		assertTrue(pushMessage.getSteps().isEmpty());
	}

	@Test
	public void aPushMessageWithoutConditionsIsTheFallback() {
		assertTrue(WebSocketPushMessage.builder().build().isFallback());
		assertFalse(WebSocketPushMessage.builder()
				.conditions(List.of(Condition.builder().operand1("${NL-MessageContent}").operator(Condition.Operator.EXISTS).build()))
				.build()
				.isFallback());
	}

	@Test
	public void theChannelFlattensItsPushMessagesAndTheirSteps() {
		final WebSocketRequest reply = WebSocketRequest.builder().name("reply").channel("actions>chat_socket").build();
		final WebSocketChannel inner = WebSocketChannel.builder().name("inner_socket").url("https://host/inner").build();
		final WebSocketPushMessage pushMessage = WebSocketPushMessage.builder().name("on_ping").addSteps(reply, inner).build();
		final WebSocketChannel channel = WebSocketChannel.builder()
				.name("chat_socket")
				.url("https://host/chat")
				.addPushMessages(pushMessage)
				.build();

		final List<Element> flattened = channel.flattened().collect(Collectors.toList());
		assertEquals(List.of(channel, pushMessage, reply, inner), flattened);
	}
}
