package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class RequestTest {
	@Test
	public void constants() {
		assertEquals("url", Request.URL);
		assertEquals("server", Request.SERVER);
		assertEquals("method", Request.METHOD);
		assertEquals("headers", Request.HEADERS);
		assertEquals("body", Request.BODY);
		
		assertEquals("#request#", Request.DEFAULT_NAME);
		assertEquals("GET", Request.DEFAULT_METHOD);
		assertEquals(Request.Method.GET.name(), Request.DEFAULT_METHOD);
	}
}
