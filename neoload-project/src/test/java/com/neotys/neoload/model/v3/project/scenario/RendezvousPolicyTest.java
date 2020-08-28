package com.neotys.neoload.model.v3.project.scenario;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RendezvousPolicyTest {

	@Test
	public void constants() {
		assertEquals("name", RendezvousPolicy.NAME);
		assertEquals("timeout", RendezvousPolicy.TIMEOUT);
		assertEquals("when", RendezvousPolicy.WHEN);
	}

}
