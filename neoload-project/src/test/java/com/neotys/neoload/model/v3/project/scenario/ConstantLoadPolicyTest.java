package com.neotys.neoload.model.v3.project.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;


public class ConstantLoadPolicyTest {
	@Test
	public void constants() {
		assertEquals("users", ConstantLoadPolicy.USERS);
		assertEquals("rampup", ConstantLoadPolicy.RAMPUP);
	}
}
