package com.neotys.neoload.model.v3.binding.converter;


import com.neotys.neoload.model.v3.project.scenario.WhenRelease;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease.Type;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class WhenReleaseToStringConverterTest {
	private static final String ZERO = "0";
	
	private static WhenRelease convertToWhenReleasePercentage(final String input) {
		return WhenRelease.builder()
				.value(input)
				.type(Type.PERCENTAGE)
				.build();
	}
	
	private static WhenRelease convertToWhenReleaseVuNumber(final String input) {
		return WhenRelease.builder()
				.value(input)
				.type(Type.VU_NUMBER)
				.build();
	}

	private static WhenRelease convertToWhenReleaseManual(final String input) {
		return WhenRelease.builder()
				.value(input)
				.type(Type.MANUAL)
				.build();
	}

	@Test
	public void shouldConvertCorrectly() {
		final WhenReleaseToStringConverter converter = new WhenReleaseToStringConverter();
		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));

		// TIME
		// Input: -1 - Output: 0O
//		assertEquals(ZERO, converter.convert(convertToWhenReleaseVuNumber("-1")));
		// Input: 0 - Output: 0
		assertEquals(ZERO, converter.convert(convertToWhenReleaseVuNumber("0")));
		assertEquals("20", converter.convert(convertToWhenReleaseVuNumber("20")));


		assertEquals("20%", converter.convert(convertToWhenReleasePercentage("20")));

		assertEquals("manual", converter.convert(convertToWhenReleaseManual("manual")));
	}
}
