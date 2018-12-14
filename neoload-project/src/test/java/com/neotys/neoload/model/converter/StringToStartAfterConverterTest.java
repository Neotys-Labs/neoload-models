package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.StartAfter;
import com.neotys.neoload.model.scenario.StartAfter.Type;


public class StringToStartAfterConverterTest {
	private static final StartAfter ERROR = StartAfter.builder().build();

	private static StartAfter convertToStartAfter(final int input) {
		return StartAfter.builder()
				.value(input)
				.type(Type.TIME)
				.build();
	}
	
	private static StartAfter convertToStartAfter(final String input) {
		return StartAfter.builder()
				.value(input)
				.type(Type.POPULATION)
				.build();
	}

	@Test
	public void shouldConvertCorrectly() {
		final StringToStartAfterConverter converter = new StringToStartAfterConverter();
		// Input: NULL - Output: -1
		assertEquals(ERROR, converter.convert(null));
		// Input: EMPTY - Output: -1
		assertEquals(ERROR, converter.convert(""));

		// TIME
		// Input: 48h30m15s - Output: 48 * 60 * 60 + 30 * 60 + 15
		assertEquals(convertToStartAfter(48 * 60 * 60 + 30 * 60 + 15), converter.convert("48h30m15s"));
		// Input: 0h30m15s - Output: 30 * 60 + 15
		assertEquals(convertToStartAfter(30 * 60 + 15), converter.convert("0h30m15s"));
		// Input: 0h0m15s - Output: 15
		assertEquals(convertToStartAfter(15), converter.convert("0h0m15s"));
		// Input: 0h0m0s - Output: 0
		assertEquals(convertToStartAfter(0), converter.convert("0h0m0s"));

		// Input: 48h30m - Output: 48 * 60 * 60 + 30 * 60
		assertEquals(convertToStartAfter(48 * 60 * 60 + 30 * 60), converter.convert("48h30m"));
		// Input: 0h30m - Output: 30 * 60
		assertEquals(convertToStartAfter(30 * 60), converter.convert("0h30m"));
		// Input: 0h0m - Output: 0
		assertEquals(convertToStartAfter(0), converter.convert("0h0m"));
	
		// Input: 30m15s - Output: 30 * 60 + 15
		assertEquals(convertToStartAfter(30 * 60 + 15), converter.convert("30m15s"));
		// Input: 0m15s - Output: 15
		assertEquals(convertToStartAfter(15), converter.convert("0m15s"));
		// Input: 0m0s - Output: 0
		assertEquals(convertToStartAfter(0), converter.convert("0m0s"));

		// Input: 48h - Output: 48 * 60 * 60
		assertEquals(convertToStartAfter(48 * 60 * 60), converter.convert("48h"));
		// Input: 90m - Output: 90 * 60
		assertEquals(convertToStartAfter(90 * 60), converter.convert("90m"));
		// Input: 15s - Output: 15
		assertEquals(convertToStartAfter(15), converter.convert("15s"));

		// Input: 0h - Output: 0
		assertEquals(convertToStartAfter(0), converter.convert("0h"));
		// Input: 0m - Output: 0
		assertEquals(convertToStartAfter(0), converter.convert("0m"));
		// Input: 0s - Output: 0
		assertEquals(convertToStartAfter(0), converter.convert("0s"));

		// Input: 900 - Output: 900 (15 minutes)
		assertEquals(convertToStartAfter(900), converter.convert("900"));

		// Input: 0 - Output: 0 
		assertEquals(convertToStartAfter(0), converter.convert("0"));
		
		// Input: -1 - Output: -1
		assertEquals(convertToStartAfter("-1"), converter.convert("-1"));

		// POPULATION
		// Input: population - Output: population
		assertEquals(convertToStartAfter("population"), converter.convert("population"));
	}
}
