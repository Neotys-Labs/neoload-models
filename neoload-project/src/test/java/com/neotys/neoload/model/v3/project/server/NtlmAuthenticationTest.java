package com.neotys.neoload.model.v3.project.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;


public class NtlmAuthenticationTest {
	@Test
	public void constants() {
		assertEquals("domain", NtlmAuthentication.DOMAIN);
	}
}
