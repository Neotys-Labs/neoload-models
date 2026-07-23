package com.neotys.neoload.model.v3.project.userpath;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WaitUntilTest {
	@Test
	public void constants() {
		assertEquals("name", WaitUntil.NAME);
		assertEquals("description", WaitUntil.DESCRIPTION);

		assertEquals("conditions", WaitUntil.CONDITIONS);
		assertEquals("timeout", WaitUntil.TIMEOUT);

		assertEquals("wait_until", WaitUntil.DEFAULT_NAME);
		assertEquals("60000", WaitUntil.DEFAULT_TIMEOUT);
	}
}
