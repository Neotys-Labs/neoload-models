package com.neotys.neoload.model.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.ConstantLoadPolicy;


public class ConstantLoadPolicyTest {
	@Test
	public void constants() {
		assertEquals("users", ConstantLoadPolicy.USERS);
		assertEquals("rampup", ConstantLoadPolicy.RAMPUP);
	}
}
