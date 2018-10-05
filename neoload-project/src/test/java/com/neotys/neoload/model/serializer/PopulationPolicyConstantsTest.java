package com.neotys.neoload.model.serializer;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class PopulationPolicyConstantsTest {
	@Test
	public void constants() {
		assertEquals("name", PopulationPolicyConstants.FIELD_NAME);
		assertEquals("constant_load", PopulationPolicyConstants.FIELD_CONSTANT_LOAD);
		assertEquals("rampup_load", PopulationPolicyConstants.FIELD_RAMPUP_LOAD);
		assertEquals("peaks_load", PopulationPolicyConstants.FIELD_PEAKS_LOAD);
	}
}
