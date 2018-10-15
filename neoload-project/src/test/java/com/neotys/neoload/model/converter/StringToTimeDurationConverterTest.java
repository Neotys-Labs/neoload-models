package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.converter.StringToTimeDurationConverter;


public class StringToTimeDurationConverterTest {
	private static final Integer ERROR = Integer.MIN_VALUE;
	
	@Test
	public void shouldConvertCorrectly() {
		final StringToTimeDurationConverter converter = new StringToTimeDurationConverter();
		// Input: NULL - Output: ERROR
		assertEquals(ERROR, converter.convert(null));
		// Input: EMPTY - Output: ERROR
		assertEquals(ERROR, converter.convert(""));

		// Input: 48h30m15s - Output: 48 * 60 * 60 + 30 * 60 + 15
		assertEquals(48 * 60 * 60 + 30 * 60 + 15, converter.convert("48h30m15s").intValue());
		// Input: 0h30m15s - Output: 30 * 60 + 15
		assertEquals(30 * 60 + 15, converter.convert("0h30m15s").intValue());
		// Input: 0h0m15s - Output: 15
		assertEquals(15, converter.convert("0h0m15s").intValue());
		// Input: 0h0m0s - Output: 0
		assertEquals(0, converter.convert("0h0m0s").intValue());

		// Input: 48h30m - Output: 48 * 60 * 60 + 30 * 60
		assertEquals(48 * 60 * 60 + 30 * 60, converter.convert("48h30m").intValue());
		// Input: 0h30m - Output: 30 * 60
		assertEquals(30 * 60, converter.convert("0h30m").intValue());
		// Input: 0h0m - Output: 0
		assertEquals(0, converter.convert("0h0m").intValue());
	
		// Input: 30m15s - Output: 30 * 60 + 15
		assertEquals(30 * 60 + 15, converter.convert("30m15s").intValue());
		// Input: 0m15s - Output: 15
		assertEquals(15, converter.convert("0m15s").intValue());
		// Input: 0m0s - Output: 0
		assertEquals(0, converter.convert("0m0s").intValue());

		// Input: 48h - Output: 48 * 60 * 60
		assertEquals(48 * 60 * 60, converter.convert("48h").intValue());
		// Input: 90m - Output: 90 * 60
		assertEquals(90 * 60, converter.convert("90m").intValue());
		// Input: 15s - Output: 15
		assertEquals(15, converter.convert("15s").intValue());

		// Input: 0h - Output: 0
		assertEquals(0, converter.convert("0h").intValue());
		// Input: 0m - Output: 0
		assertEquals(0, converter.convert("0m").intValue());
		// Input: 0s - Output: 0
		assertEquals(0, converter.convert("0s").intValue());

		// Input: 900 - Output: 900 (15 minutes)
		assertEquals(900, converter.convert("900").intValue());

		// Input: -1 - Output: ERROR
		assertEquals(ERROR, converter.convert("-1"));
		// Input: xXx - Output: ERROR
		assertEquals(ERROR, converter.convert("xXx"));
		// Input: x48hx30m - Output: ERROR
		assertEquals(ERROR, converter.convert("x48hx30m"));
	}
}
