package com.neotys.neoload.model.v3.binding.converter;

import org.junit.Test;

import static com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationInMsOrInVariableConverter.STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
import static org.junit.Assert.*;

public class StringToTimeDurationInMsOrInVariableConverterTest {

	@Test
	public void testConverts() {
		final StringToTimeDurationInMsOrInVariableConverter converter = STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE;
		assertEquals("0", converter.convert(null));
		assertEquals("0", converter.convert("-1000"));
		assertEquals("0", converter.convert(""));
		
		assertEquals("${variable}", converter.convert("${variable}"));
		assertEquals("60300", converter.convert("1m 300ms"));
	}
}