package com.neotys.neoload.model.v3.project.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.server.BasicAuthentication;


public class BasicAuthenticationTest {
	@Test
	public void constants() {
		assertEquals("realm", BasicAuthentication.REALM);
	}
}
