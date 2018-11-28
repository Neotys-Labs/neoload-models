package com.neotys.neoload.model.v3.project.server;


import org.junit.Test;

import static com.neotys.neoload.model.v3.project.server.Authentication.BASIC_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Authentication.NEGOCIATE_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Authentication.NTLM_AUTHENTICATION;
import static org.junit.Assert.assertEquals;


public class ServerTest {
	@Test
	public void constants() {
		assertEquals("name", Server.NAME);
		assertEquals("host", Server.HOST);
		assertEquals("port", Server.PORT);
		assertEquals("scheme", Server.SCHEME);
		
		assertEquals("authentication", Server.AUTHENTICATION);
		assertEquals("basic_authentication", BASIC_AUTHENTICATION);
		assertEquals("ntlm_authentication", NTLM_AUTHENTICATION);
		assertEquals("negotiate_authentication", NEGOCIATE_AUTHENTICATION);
		
		assertEquals("80", Server.DEFAULT_PORT);
		assertEquals(Server.Scheme.HTTP, Server.DEFAULT_SCHEME);
		
		assertEquals("http", Server.SCHEME_HTTP);
		assertEquals("https", Server.SCHEME_HTTPS);
	}
}
