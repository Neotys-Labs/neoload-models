package com.neotys.neoload.model.v3.project.userpath;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import org.junit.Test;

public class WaitUntilTest {
	@Test
	public void constants() {
		assertEquals("name", WaitUntil.NAME);
		assertEquals("description", WaitUntil.DESCRIPTION);

		assertEquals("conditions", WaitUntil.CONDITIONS);
		assertEquals("timeout", WaitUntil.TIMEOUT);

		assertEquals("wait_until", WaitUntil.DEFAULT_NAME);
		assertEquals(DurationLiteral.builder().value(Duration.ofSeconds(60)).build(), WaitUntil.DEFAULT_TIMEOUT);
	}
}
