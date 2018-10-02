package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;


public class StringToTimeDurationConverterTest {
	private Optional<Integer> of(final int value) {
		return Optional.of(Integer.valueOf(value));
	}

	@Test
	public void shouldConvertCorrectly() {
		final StringToTimeDurationConverter converter = new StringToTimeDurationConverter();
		// Input: NULL - Output: Optional.empty()
		assertEquals(Optional.empty(), converter.convert(null));
		// Input: EMPTY - Output: Optional.empty()
		assertEquals(Optional.empty(), converter.convert(""));

		// Input: 48h30m15s - Output: 48 * 60 * 60 + 30 * 60 + 15
		assertEquals(of(48 * 60 * 60 + 30 * 60 + 15), converter.convert("48h30m15s"));
		// Input: 0h30m15s - Output: 30 * 60 + 15
		assertEquals(of(30 * 60 + 15), converter.convert("0h30m15s"));
		// Input: 0h0m15s - Output: 15
		assertEquals(of(15), converter.convert("0h0m15s"));
		// Input: 0h0m0s - Output: 0
		assertEquals(of(0), converter.convert("0h0m0s"));

		// Input: 48h30m - Output: 48 * 60 * 60 + 30 * 60
		assertEquals(of(48 * 60 * 60 + 30 * 60), converter.convert("48h30m"));
		// Input: 0h30m - Output: 30 * 60
		assertEquals(of(30 * 60), converter.convert("0h30m"));
		// Input: 0h0m - Output: 0
		assertEquals(of(0), converter.convert("0h0m"));
	
		// Input: 30m15s - Output: 30 * 60 + 15
		assertEquals(of(30 * 60 + 15), converter.convert("30m15s"));
		// Input: 0m15s - Output: 15
		assertEquals(of(15), converter.convert("0m15s"));
		// Input: 0m0s - Output: 0
		assertEquals(of(0), converter.convert("0m0s"));

		// Input: 48h - Output: 48 * 60 * 60
		assertEquals(of(48 * 60 * 60), converter.convert("48h"));
		// Input: 90m - Output: 90 * 60
		assertEquals(of(90 * 60), converter.convert("90m"));
		// Input: 15s - Output: 15
		assertEquals(of(15), converter.convert("15s"));

		// Input: 0h - Output: 0
		assertEquals(of(0), converter.convert("0h"));
		// Input: 0m - Output: 0
		assertEquals(of(0), converter.convert("0m"));
		// Input: 0s - Output: 0
		assertEquals(of(0), converter.convert("0s"));

		// Input: 900 - Output: 900 (15 minutes)
		assertEquals(of(900), converter.convert("900"));

		// Input: -1 - Output: Optional.empty()
		assertEquals(Optional.empty(), converter.convert("-1"));
		// Input: xXx - Output: Optional.empty()
		assertEquals(Optional.empty(), converter.convert("xXx"));
		// Input: x48hx30m - Output: Optional.empty()
		assertEquals(Optional.empty(), converter.convert("x48hx30m"));
	}
}
