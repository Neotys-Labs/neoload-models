package com.neotys.neoload.model.v3.project.scenario;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MonitoringParametersTest {

	@Test
	public void constants() {
		assertEquals("before", MonitoringParameters.BEFORE_FIRST);
		assertEquals("after", MonitoringParameters.AFTER_LAST);
	}

}
