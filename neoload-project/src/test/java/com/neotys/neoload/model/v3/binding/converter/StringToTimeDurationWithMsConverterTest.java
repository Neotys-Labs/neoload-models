package com.neotys.neoload.model.v3.binding.converter;

import org.junit.Test;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationWithMsConverter.STRING_TO_TIME_DURATION_WITH_MS;
import static org.junit.Assert.*;

public class StringToTimeDurationWithMsConverterTest {

	@Test
	public void testConverts() {
		final StringToTimeDurationWithMsConverter converter = STRING_TO_TIME_DURATION_WITH_MS;
		assertEquals("0", converter.convert(null));
		assertEquals("0", converter.convert(""));
		assertEquals("${variable}", converter.convert("${variable}"));
		assertEquals("60300", converter.convert("1m 300ms"));
	}
}