package com.neotys.neoload.model.v3.binding.converter;


import com.neotys.neoload.model.v3.project.scenario.WhenRelease;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class StringToWhenReleaseConverterTest {
	private static final WhenRelease ERROR = WhenRelease.builder().build();
	
	private static final WhenRelease MANUAL = WhenRelease.builder()
			.type(WhenRelease.Type.MANUAL)
			.build();

	private static WhenRelease convertToWhenReleasePercentage(final String input) {
		return WhenRelease.builder()
				.value(input)
				.type(WhenRelease.Type.PERCENTAGE)
				.build();
	}

	private static WhenRelease convertToWhenReleaseVuNumber(final String input) {
		return WhenRelease.builder()
				.value(input)
				.type(WhenRelease.Type.VU_NUMBER)
				.build();
	}

	private static WhenRelease convertToWhenReleaseManual(final String input) {
		return WhenRelease.builder()
				.value(input)
				.type(WhenRelease.Type.MANUAL)
				.build();
	}

	@Test
	public void shouldConvertCorrectly() {
		final StringToWhenReleaseConverter converter = new StringToWhenReleaseConverter();
		// Input: NULL - Output: -1
		assertEquals(ERROR, converter.convert(null));
		// Input: EMPTY - Output: -1
		assertEquals(ERROR, converter.convert(""));

		assertEquals(ERROR, converter.convert("0"));

		// MANUAL
		assertEquals(convertToWhenReleaseManual("manual"), converter.convert("manual"));
		assertEquals(convertToWhenReleaseManual("manual"), converter.convert("Manual"));

		//PERCENTAGE
		assertEquals(convertToWhenReleasePercentage("0"), converter.convert("0%"));
		assertEquals(convertToWhenReleasePercentage("5"), converter.convert("5%"));
		assertEquals(convertToWhenReleasePercentage("05"), converter.convert("05%"));
		assertEquals(convertToWhenReleasePercentage("20"), converter.convert("20%"));
		assertEquals(convertToWhenReleasePercentage("100"), converter.convert("100%"));

		//VU NUMBER
		assertEquals(convertToWhenReleaseVuNumber("5"), converter.convert("5"));
		assertEquals(convertToWhenReleaseVuNumber("20"), converter.convert("20"));
		assertEquals(convertToWhenReleaseVuNumber("100"), converter.convert("100"));


	}
}
