package com.neotys.neoload.model.v3.project.server;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class NegociateAuthenticationTest {
	@Test
	public void constants() {
		assertEquals("domain", NegotiateAuthentication.DOMAIN);
	}
}
