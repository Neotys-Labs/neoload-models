package com.neotys.neoload.model.listener;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SupportLevelCounterTest {

	@Test
	public void test() {
		final SupportLevelCounter counter = new SupportLevelCounter();
		assertEquals("supported: 0 (without warning) + 0 (with warning). Unsupported: 0", counter.getCurrentSummary());
		assertEquals("\tSupported: 0 (without warning) + 0 (with warning). \n\tUnsupported: 0\n", counter.getTotalSummary());
		assertEquals("0%", counter.getTotalCoveragePercent());
		counter.nextScript();
		counter.readSupported("toto");
		assertEquals("supported: 1 (without warning) + 0 (with warning). Unsupported: 0", counter.getCurrentSummary());
		assertEquals("\tSupported: 1 (without warning) + 0 (with warning). \n\tUnsupported: 0\n", counter.getTotalSummary());
		assertEquals("100%", counter.getTotalCoveragePercent());
		counter.readUnsupported("titi");
		assertEquals("supported: 1 (without warning) + 0 (with warning). Unsupported: 1", counter.getCurrentSummary());
		assertEquals("\tSupported: 1 (without warning) + 0 (with warning). \n\tUnsupported: 1\n			*1: titi\n", counter.getTotalSummary());
		assertEquals("50%", counter.getTotalCoveragePercent());
		
	}

}
