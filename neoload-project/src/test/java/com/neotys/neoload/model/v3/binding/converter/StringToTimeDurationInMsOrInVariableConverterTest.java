package com.neotys.neoload.model.v3.binding.converter;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;

public class StringToTimeDurationInMsOrInVariableConverterTest {

	@Test
	public void testConverts() {
		final StringToTimeDurationInMsOrInVariableConverter converter = STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
		
		assertEquals("${variable}", converter.convert("${variable}"));
		assertEquals("60300", converter.convert("1m 300ms"));
		assertEquals("5415000", converter.convert(" 1h 30m 15s "));
		assertEquals("1500", converter.convert(" 1500 "));
		assertEquals("${variable}", converter.convert(" ${variable} "));
	}

	@Test
	public void testKeepsInvalidDurationsAsWritten() {
		for (final String input : Arrays.asList("", "-1000", "1 h", "1m1h", "1s1s", "1s5", "99999999999999999999")) {
			assertEquals(input, STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE.convert(input));
		}
	}
}
