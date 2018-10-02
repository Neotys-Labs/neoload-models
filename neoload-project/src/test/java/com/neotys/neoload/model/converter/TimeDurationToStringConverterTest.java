package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;


public class TimeDurationToStringConverterTest {
	private static final String ZERO = "0";

	private Optional<Integer> of(final int value) {
		return Optional.of(Integer.valueOf(value));
	}
	
	@Test
	public void shouldConvertCorrectly() {
		final TimeDurationToStringConverter converter = new TimeDurationToStringConverter();
		// Input: NULL - Output: 0
		assertEquals(null, converter.convert(Optional.empty()));
		// Input: -1 - Output: 0
		assertEquals(ZERO, converter.convert(of(-1)));
		// Input: 0 - Output: 0
		assertEquals(ZERO, converter.convert(of(0)));

		// Input: 30 - Output: 0h0m30s
		assertEquals("30s", converter.convert(of(30)));
		// Input: 30 * 60 - Output: 0h30m
		assertEquals("30m", converter.convert(of(30 * 60)));
		// Input: 48 * 60 * 60 - Output: 48h0m
		assertEquals("48h", converter.convert(of(48 * 60 * 60)));
		// Input: 30 * 60 + 30 - Output: 30m30s
		assertEquals("30m30s", converter.convert(of(30 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 - Output: 48h30s
		assertEquals("48h30s", converter.convert(of(48 * 60 * 60 + 30)));
		// Input: 48 * 60 * 60 + 30 * 60 - Output: 48h30m
		assertEquals("48h30m", converter.convert(of(48 * 60 * 60 + 30 * 60)));
		// Input: 48 * 60 * 60 + 30 * 60 + 30 - Output: 48h30m30s
		assertEquals("48h30m30s", converter.convert(of(48 * 60 * 60 + 30 * 60 + 30)));
	}
}
