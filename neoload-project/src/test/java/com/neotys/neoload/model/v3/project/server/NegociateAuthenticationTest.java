package com.neotys.neoload.model.v3.project.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.server.NegociateAuthentication;


public class NegociateAuthenticationTest {
	@Test
	public void constants() {
		assertEquals("login", NegociateAuthentication.LOGIN);
		assertEquals("password", NegociateAuthentication.PASSWORD);
		assertEquals("domain", NegociateAuthentication.DOMAIN);
	}
}
