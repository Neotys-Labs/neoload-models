package com.neotys.neoload.model.v3.project.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class LoginPasswordAuthenticationTest {
	@Test
	public void constants() {
		assertEquals("basic_authentication", LoginPasswordAuthentication.BASIC_AUTHENTICATION);
		assertEquals("negotiate_authentication", LoginPasswordAuthentication.NEGOTIATE_AUTHENTICATION);
		assertEquals("ntlm_authentication", LoginPasswordAuthentication.NTLM_AUTHENTICATION);

		assertEquals("login", LoginPasswordAuthentication.LOGIN);
		assertEquals("password", LoginPasswordAuthentication.PASSWORD);
	}
}
