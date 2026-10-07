package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class WebSocketRequestTest {
	@Test
	public void constants() {
		assertEquals("name", WebSocketRequest.NAME);
		assertEquals("description", WebSocketRequest.DESCRIPTION);
		assertEquals("channel", WebSocketRequest.CHANNEL);
		assertEquals("message_type", WebSocketRequest.MESSAGE_TYPE);
		assertEquals("synchronous", WebSocketRequest.SYNCHRONOUS);
		assertEquals("mapping_id", WebSocketRequest.MAPPING_ID);
		assertEquals("status_code", WebSocketRequest.STATUS_CODE);
		assertEquals("body", WebSocketRequest.BODY);
		assertEquals("bodybinary", WebSocketRequest.BODYBINARY);
		assertEquals("extractors", WebSocketRequest.EXTRACTORS);
	}

	@Test
	public void defaults() {
		final WebSocketRequest request = WebSocketRequest.builder().build();
		assertEquals(WebSocketRequest.DEFAULT_NAME, request.getName());
		assertEquals(WebSocketRequest.MessageType.TEXT, request.getMessageType());
		assertFalse(request.isSynchronous());
		assertEquals("1000", request.getStatusCode());
	}
}
