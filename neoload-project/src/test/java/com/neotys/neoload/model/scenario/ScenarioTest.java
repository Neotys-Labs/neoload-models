package com.neotys.neoload.model.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.Scenario;


public class ScenarioTest {
	@Test
	public void constants() {
		assertEquals("sla_profile", Scenario.SLA_PROFILE);
		assertEquals("populations", Scenario.POPULATIONS);
	}
}
