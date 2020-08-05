package com.neotys.neoload.model.v3.project.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class PeakLoadPolicyTest {
	@Test
	public void constants() {
		assertEquals("users", PeakLoadPolicy.USERS);
		assertEquals("duration", PeakLoadPolicy.DURATION);
	}
}
