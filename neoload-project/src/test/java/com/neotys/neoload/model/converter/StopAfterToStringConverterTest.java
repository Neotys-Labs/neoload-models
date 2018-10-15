package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.scenario.StopAfter;
import com.neotys.neoload.model.scenario.StopAfter.Type;


public class StopAfterToStringConverterTest {
	private static final String ZERO = "0";
	
	private static StopAfter convertToStopAfter(final int input) {
		return StopAfter.builder()
				.value(input)
				.type(Type.TIME)
				.build();
	}
	
	private static StopAfter convertToStopAfter() {
		return StopAfter.builder()
				.type(Type.CURRENT_ITERATION)
				.build();
	}

	@Test
	public void shouldConvertCorrectly() {
		final StopAfterToStringConverter converter = new StopAfterToStringConverter();
		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));

		// TIME
		// Input: -1 - Output: 0O
		assertEquals(ZERO, converter.convert(convertToStopAfter(-1)));
		// Input: 0 - Output: 0
		assertEquals(ZERO, converter.convert(convertToStopAfter(0)));

		// Input: 30 - Output: 0h0m30s
		assertEquals("30s", converter.convert(convertToStopAfter(30)));
		// Input: 30 * 60 - Output: 0h30m
		assertEquals("30m", converter.convert(convertToStopAfter(30 * 60)));
		// Input: 48 * 60 * 60 - Output: 48h0m
		assertEquals("48h", converter.convert(convertToStopAfter(48 * 60 * 60)));
		// Input: 30 * 60 + 30 - Output: 30m30s
		assertEquals("30m30s", converter.convert(convertToStopAfter(30 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 - Output: 48h30s
		assertEquals("48h30s", converter.convert(convertToStopAfter(48 * 60 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 * 60 - Output: 48h30m
		assertEquals("48h30m", converter.convert(convertToStopAfter(48 * 60 * 60 + 30 * 60)));
		// Input: 48 * 60 * 60 + 30 * 60 + 30 - Output: 48h30m30s
		assertEquals("48h30m30s", converter.convert(convertToStopAfter(48 * 60 * 60 + 30 * 60 + 30)));
		
		// CURRENT ITERATION
		// Input: current_iteration - Output: current_iteration
		assertEquals("current_iteration", converter.convert(convertToStopAfter()));
	}
}
