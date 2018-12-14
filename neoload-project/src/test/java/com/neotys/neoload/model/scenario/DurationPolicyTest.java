package com.neotys.neoload.model.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.DurationPolicy;


public class DurationPolicyTest {
	@Test
	public void constants() {
		assertEquals("duration", DurationPolicy.DURATION);
	}
}
