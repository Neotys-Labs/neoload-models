package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WebSocketChannelTest {
	@Test
	public void constants() {
		assertEquals("name", WebSocketChannel.NAME);
		assertEquals("description", WebSocketChannel.DESCRIPTION);
		assertEquals("url", WebSocketChannel.URL);
		assertEquals("server", WebSocketChannel.SERVER);
		assertEquals("headers", WebSocketChannel.HEADERS);
		assertEquals("extractors", WebSocketChannel.EXTRACTORS);
		assertEquals("messages_mapping", WebSocketChannel.MESSAGES_MAPPING);
		assertEquals("timestamp_extractor", WebSocketChannel.TIMESTAMP_EXTRACTOR);
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

	@Test
	public void timestampExtractorConstantsAndDefaults() {
		assertEquals("regexp", WebSocketTimestampExtractor.REGEXP);
		assertEquals("template", WebSocketTimestampExtractor.TEMPLATE);
		assertEquals(VariableExtractor.DEFAULT_TEMPLATE_VALUE, WebSocketTimestampExtractor.DEFAULT_TEMPLATE_VALUE);

		final WebSocketTimestampExtractor extractor = WebSocketTimestampExtractor.builder().regexp("ts=(\\d+)").build();
		assertEquals(VariableExtractor.DEFAULT_TEMPLATE_VALUE, extractor.getTemplate());
	}
}
