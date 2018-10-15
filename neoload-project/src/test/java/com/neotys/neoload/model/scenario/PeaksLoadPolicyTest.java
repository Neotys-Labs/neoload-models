package com.neotys.neoload.model.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.PeaksLoadPolicy;


public class PeaksLoadPolicyTest {
	@Test
	public void constants() {
		assertEquals("minimum", PeaksLoadPolicy.MINIMUM);
		assertEquals("maximum", PeaksLoadPolicy.MAXIMUM);
		assertEquals("start", PeaksLoadPolicy.START);
		assertEquals("step_rampup", PeaksLoadPolicy.STEP_RAMPUP);
	}
}
