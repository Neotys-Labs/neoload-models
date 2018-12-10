package com.neotys.neoload.model.v3.project.server;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ServerTest {
	@Test
	public void constants() {
		assertEquals("name", Server.NAME);
		assertEquals("host", Server.HOST);
		assertEquals("port", Server.PORT);
		assertEquals("scheme", Server.SCHEME);
		
		assertEquals("80", Server.DEFAULT_HTTP_PORT);
		assertEquals("443", Server.DEFAULT_HTTPS_PORT);
		assertEquals(Server.Scheme.HTTP, Server.DEFAULT_SCHEME);
	}
}
