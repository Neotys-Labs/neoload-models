package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.converter.DurationToStringConverter;
import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.scenario.Duration.Type;


public class DurationToStringConverterTest {
	private static final String ZERO = "0";
	
	private Duration convertToTimeDuration(final int input) {
		return Duration.builder()
				.value(input)
				.type(Type.TIME)
				.build();
	}
	
	private Duration convertToIterationDuration(final int input) {
		return Duration.builder()
				.value(input)
				.type(Type.ITERATION)
				.build();
	}

	@Test
	public void shouldConvertCorrectly() {
		final DurationToStringConverter converter = new DurationToStringConverter();

		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));

		// TIME
		// Input: -1 - Output: 0O
		assertEquals(ZERO, converter.convert(convertToTimeDuration(-1)));
		// Input: 0 - Output: 0
		assertEquals(ZERO, converter.convert(convertToTimeDuration(0)));

		// Input: 30 - Output: 0h0m30s
		assertEquals("30s", converter.convert(convertToTimeDuration(30)));
		// Input: 30 * 60 - Output: 0h30m
		assertEquals("30m", converter.convert(convertToTimeDuration(30 * 60)));
		// Input: 48 * 60 * 60 - Output: 48h0m
		assertEquals("48h", converter.convert(convertToTimeDuration(48 * 60 * 60)));
		// Input: 30 * 60 + 30 - Output: 30m30s
		assertEquals("30m30s", converter.convert(convertToTimeDuration(30 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 - Output: 48h30s
		assertEquals("48h30s", converter.convert(convertToTimeDuration(48 * 60 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 * 60 - Output: 48h30m
		assertEquals("48h30m", converter.convert(convertToTimeDuration(48 * 60 * 60 + 30 * 60)));
		// Input: 48 * 60 * 60 + 30 * 60 + 30 - Output: 48h30m30s
		assertEquals("48h30m30s", converter.convert(convertToTimeDuration(48 * 60 * 60 + 30 * 60 + 30)));
		
		// ITERATION
		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));
		// Input: NULL - Output: 0
		assertEquals(null, converter.convert(convertToIterationDuration(-1)));
		// Input: NULL - Output: 0
		assertEquals(null, converter.convert(convertToIterationDuration(0)));

		// Input: 1 - Output: 1  iteration
		assertEquals("1 iteration", converter.convert(convertToIterationDuration(1)));
		// Input: 2 - Output: 2 iteration
		assertEquals("2 iterations", converter.convert(convertToIterationDuration(2)));
		// Input: 30 - Output: 30 iteration
		assertEquals("30 iterations", converter.convert(convertToIterationDuration(30)));
	}
}
