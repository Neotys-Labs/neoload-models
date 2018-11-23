package com.neotys.neoload.model.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.server.NtlmAuthentication;


public class NtlmAuthenticationTest {
	@Test
	public void constants() {
		assertEquals("login", NtlmAuthentication.LOGIN);
		assertEquals("password", NtlmAuthentication.PASSWORD);
		assertEquals("domain", NtlmAuthentication.DOMAIN);
	}
}
