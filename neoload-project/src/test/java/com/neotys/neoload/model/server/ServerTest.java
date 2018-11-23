package com.neotys.neoload.model.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.server.Server;


public class ServerTest {
	@Test
	public void constants() {
		assertEquals("name", Server.NAME);
		assertEquals("host", Server.HOST);
		assertEquals("port", Server.PORT);
		assertEquals("scheme", Server.SCHEME);
		
		assertEquals("authentication", Server.AUTHENTICATION);
		assertEquals("basic_authentication", Server.BASIC_AUTHENTICATION);
		assertEquals("ntlm_authentication", Server.NTLM_AUTHENTICATION);
		assertEquals("negotiate_authentication", Server.NEGOCIATE_AUTHENTICATION);
		
		assertEquals("80", Server.DEFAULT_PORT);
		assertEquals(Server.Scheme.HTTP, Server.DEFAULT_SCHEME);
		
		assertEquals("http", Server.SCHEME_HTTP);
		assertEquals("https", Server.SCHEME_HTTPS);
	}
}
