package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ContainerTest {
	@Test
	public void constants() {
		assertEquals("name", Container.NAME);
		assertEquals("description", Container.DESCRIPTION);

		assertEquals("sla_profile", Container.SLA_PROFILE);

		assertEquals("steps", Container.STEPS);

		assertEquals("assertions", Container.ASSERTIONS);
	}
}
