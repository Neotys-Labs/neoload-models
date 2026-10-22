package com.neotys.neoload.model.v3.binding.converter;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsOrInVariableToStringConverter.TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING;
import static org.junit.Assert.*;

import org.junit.Test;

public class TimeDurationInMsOrInVariableToStringConverterTest {

	@Test
	public void testConverts() {
		final TimeDurationInMsOrInVariableToStringConverter converter = TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING;
		assertEquals("0", converter.convert(null));
		assertEquals("-1000", converter.convert("-1000"));
		assertEquals("0", converter.convert(""));
		
		assertEquals("${variable}", converter.convert("${variable}"));
		assertEquals("1m300ms", converter.convert("60300"));
		assertEquals("abc", converter.convert("abc"));
	}
}