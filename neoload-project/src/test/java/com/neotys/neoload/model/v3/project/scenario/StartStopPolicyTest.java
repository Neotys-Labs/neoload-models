package com.neotys.neoload.model.v3.project.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.StartStopPolicy;


public class StartStopPolicyTest {
	@Test
	public void constants() {
		assertEquals("start_after", StartStopPolicy.START_AFTER);
		assertEquals("stop_after", StartStopPolicy.STOP_AFTER);
	}
}
