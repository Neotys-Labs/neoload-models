package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.userpath.Request.Method;


public class RequestTest {
	@Test
	public void constants() {
		assertEquals("name", Request.NAME);
		assertEquals("url", Request.URL);
		assertEquals("server", Request.SERVER);
		assertEquals("method", Request.METHOD);
		assertEquals("headers", Request.HEADERS);
		assertEquals("body", Request.BODY);
		assertEquals("extractors", Request.EXTRACTORS);
		
		assertEquals("#request#", Request.DEFAULT_NAME);
		assertEquals("GET", Request.DEFAULT_METHOD);
		assertEquals(Request.Method.GET.name(), Request.DEFAULT_METHOD);
	}
	
	@Test
	public void methodOf() {
		boolean throwException = false;
		try {
			Method.of(null);
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}
		
		throwException = false;
		try {
			Method.of("");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}

		assertEquals(Method.GET, Method.of("GET"));
		assertEquals(Method.GET, Method.of("get"));
		
		assertEquals(Method.POST, Method.of("POST"));
		assertEquals(Method.POST, Method.of("post"));
		
		assertEquals(Method.HEAD, Method.of("HEAD"));
		assertEquals(Method.HEAD, Method.of("head"));
		
		assertEquals(Method.PUT, Method.of("PUT"));
		assertEquals(Method.PUT, Method.of("put"));
		
		assertEquals(Method.DELETE, Method.of("DELETE"));
		assertEquals(Method.DELETE, Method.of("delete"));
		
		assertEquals(Method.OPTIONS, Method.of("OPTIONS"));
		assertEquals(Method.OPTIONS, Method.of("options"));
		
		assertEquals(Method.TRACE, Method.of("TRACE"));
		assertEquals(Method.TRACE, Method.of("trace"));
		
		assertEquals(Method.CUSTOM, Method.of("CUSTOM"));
		assertEquals(Method.CUSTOM, Method.of("custom"));
		assertEquals(Method.CUSTOM, Method.of("test"));
	}
}
