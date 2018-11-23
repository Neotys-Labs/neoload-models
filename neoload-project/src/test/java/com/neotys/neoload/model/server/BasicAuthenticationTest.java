package com.neotys.neoload.model.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.server.BasicAuthentication;


public class BasicAuthenticationTest {
	@Test
	public void constants() {
		assertEquals("login", BasicAuthentication.LOGIN);
		assertEquals("password", BasicAuthentication.PASSWORD);
		assertEquals("realm", BasicAuthentication.REALM);
	}
}
