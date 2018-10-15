package com.neotys.neoload.model.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.PopulationPolicy;


public class PopulationPolicyTest {
	@Test
	public void constants() {
		assertEquals("load_policy", PopulationPolicy.LOAD_POLICY);
		assertEquals("constant_load", PopulationPolicy.CONSTANT_LOAD);
		assertEquals("rampup_load", PopulationPolicy.RAMPUP_LOAD);
		assertEquals("peaks_load", PopulationPolicy.PEAKS_LOAD);
	}
}
