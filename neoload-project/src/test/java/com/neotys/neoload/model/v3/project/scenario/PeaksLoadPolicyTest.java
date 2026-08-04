package com.neotys.neoload.model.v3.project.scenario;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy.Peak;
import org.junit.Test;



public class PeaksLoadPolicyTest {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static String wire(final Object enumValue) throws Exception {
		final String json = MAPPER.writeValueAsString(enumValue);
		return json.substring(1, json.length() - 1);
	}

	@Test
	public void constants() {
		assertEquals("minimum", PeaksLoadPolicy.MINIMUM);
		assertEquals("maximum", PeaksLoadPolicy.MAXIMUM);
		assertEquals("start", PeaksLoadPolicy.START);
		assertEquals("step_rampup", PeaksLoadPolicy.STEP_RAMPUP);
	}

	@Test
	public void peakValues() {
		assertArrayEquals(new Peak[]{Peak.MINIMUM, Peak.MAXIMUM}, Peak.values());
	}

	@Test
	public void peakWire() throws Exception {
		assertEquals("minimum", wire(Peak.MINIMUM));
		assertEquals("maximum", wire(Peak.MAXIMUM));
	}

	@Test
	public void check() {
		PeaksLoadPolicy.builder()
				.build()
				.check();
	}	
}
