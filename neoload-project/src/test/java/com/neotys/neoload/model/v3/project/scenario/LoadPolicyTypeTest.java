package com.neotys.neoload.model.v3.project.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class LoadPolicyTypeTest {
	@Test(expected=NullPointerException.class)
	public void checkedValueOf_null() {
		LoadPolicyType.checkedValueOf(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void checkedValueOf_invalid_argument() {
		LoadPolicyType.checkedValueOf("UNKNOWN");
	}

	@Test
	public void checkedValueOf() {
		assertEquals(LoadPolicyType.CONSTANT, LoadPolicyType.checkedValueOf("CONSTANT"));
		assertEquals(LoadPolicyType.CUSTOM, LoadPolicyType.checkedValueOf("CUSTOM"));
		assertEquals(LoadPolicyType.PEAKS, LoadPolicyType.checkedValueOf("PEAKS"));
		assertEquals(LoadPolicyType.RAMPUP, LoadPolicyType.checkedValueOf("RAMPUP"));
	}
}
