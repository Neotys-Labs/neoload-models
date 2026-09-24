package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WebSocketChannelTest {
	@Test
	public void constants() {
		assertEquals("name", WebSocketChannel.NAME);
		assertEquals("description", WebSocketChannel.DESCRIPTION);
		assertEquals("id", WebSocketChannel.ID);
		assertEquals("url", WebSocketChannel.URL);
		assertEquals("server", WebSocketChannel.SERVER);
		assertEquals("method", WebSocketChannel.METHOD);
		assertEquals("headers", WebSocketChannel.HEADERS);
		assertEquals("extractors", WebSocketChannel.EXTRACTORS);
		assertEquals("messages_mapping", WebSocketChannel.MESSAGES_MAPPING);
		assertEquals("push_messages", WebSocketChannel.PUSH_MESSAGES);

		assertEquals("GET", WebSocketChannel.DEFAULT_METHOD);
		assertEquals(Request.DEFAULT_METHOD, WebSocketChannel.DEFAULT_METHOD);
	}

	@Test
	public void pushMessageConstants() {
		assertEquals("push_message", WebSocketPushMessage.DEFAULT_NAME);
		assertEquals("conditions", WebSocketPushMessage.CONDITIONS);
		assertEquals("charset", WebSocketPushMessage.CHARSET);
		assertEquals("extractors", WebSocketPushMessage.EXTRACTORS);
	}

	@Test
	public void messagesMappingConstants() {
		assertEquals("xpath", WebSocketMessagesMapping.XPATH);
		assertEquals("jsonpath", WebSocketMessagesMapping.JSON_PATH);
		assertEquals("regexp", WebSocketMessagesMapping.REGEXP);
		assertEquals("template", WebSocketMessagesMapping.TEMPLATE);
		assertEquals("decode", WebSocketMessagesMapping.DECODE);
		assertEquals("custom_decoder", WebSocketMessagesMapping.CUSTOM_DECODER);
		assertEquals("encoding", WebSocketMessagesMapping.ENCODING);

		assertEquals(VariableExtractor.DEFAULT_REGEXP_VALUE, WebSocketMessagesMapping.DEFAULT_REGEXP_VALUE);
		assertEquals(VariableExtractor.DEFAULT_TEMPLATE_VALUE, WebSocketMessagesMapping.DEFAULT_TEMPLATE_VALUE);
	}

	/**
	 * Push messages are not {@link Step}s, so {@code flattened()} is overridden to keep them
	 * visible to the tree walk the reference validators rely on.
	 */
	@Test
	public void flattenedIncludesPushMessages() {
		final WebSocketChannel channel = WebSocketChannel.builder()
				.name("my_channel")
				.id("ws_main")
				.url("wss://host:443/socket")
				.addPushMessages(WebSocketPushMessage.builder().name("first").build())
				.addPushMessages(WebSocketPushMessage.builder().name("second").build())
				.build();

		assertEquals(3, channel.flattened().count());
	}
}
