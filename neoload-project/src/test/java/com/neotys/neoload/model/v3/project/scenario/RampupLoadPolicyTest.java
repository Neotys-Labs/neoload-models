package com.neotys.neoload.model.v3.project.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;


public class RampupLoadPolicyTest {
	@Test
	public void constants() {
		assertEquals("min_users", RampupLoadPolicy.MIN_USERS);
		assertEquals("max_users", RampupLoadPolicy.MAX_USERS);
		assertEquals("increment_users", RampupLoadPolicy.INCREMENT_USERS);
		assertEquals("increment_every", RampupLoadPolicy.INCREMENT_EVERY);
		assertEquals("increment_rampup", RampupLoadPolicy.INCREMENT_RAMPUP);
	}
}
