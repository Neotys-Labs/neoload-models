package com.neotys.neoload.model.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.StartStopPolicy;


public class StartStopPolicyTest {
	@Test
	public void constants() {
		assertEquals("start_after", StartStopPolicy.START_AFTER);
		assertEquals("stop_after", StartStopPolicy.STOP_AFTER);
	}
}
