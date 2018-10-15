package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.converter.StartAfterToStringConverter;
import com.neotys.neoload.model.scenario.StartAfter;
import com.neotys.neoload.model.scenario.StartAfter.Type;


public class StartAfterToStringConverterTest {
	private static final String ZERO = "0";
	
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
		final StartAfterToStringConverter converter = new StartAfterToStringConverter();
		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));

		// TIME
		// Input: -1 - Output: 0O
		assertEquals(ZERO, converter.convert(convertToStartAfter(-1)));
		// Input: 0 - Output: 0
		assertEquals(ZERO, converter.convert(convertToStartAfter(0)));

		// Input: 30 - Output: 0h0m30s
		assertEquals("30s", converter.convert(convertToStartAfter(30)));
		// Input: 30 * 60 - Output: 0h30m
		assertEquals("30m", converter.convert(convertToStartAfter(30 * 60)));
		// Input: 48 * 60 * 60 - Output: 48h0m
		assertEquals("48h", converter.convert(convertToStartAfter(48 * 60 * 60)));
		// Input: 30 * 60 + 30 - Output: 30m30s
		assertEquals("30m30s", converter.convert(convertToStartAfter(30 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 - Output: 48h30s
		assertEquals("48h30s", converter.convert(convertToStartAfter(48 * 60 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 * 60 - Output: 48h30m
		assertEquals("48h30m", converter.convert(convertToStartAfter(48 * 60 * 60 + 30 * 60)));
		// Input: 48 * 60 * 60 + 30 * 60 + 30 - Output: 48h30m30s
		assertEquals("48h30m30s", converter.convert(convertToStartAfter(48 * 60 * 60 + 30 * 60 + 30)));
		
		// POPULATION
		// Input: population - Output: population
		assertEquals("population", converter.convert(convertToStartAfter("population")));
	}
}
