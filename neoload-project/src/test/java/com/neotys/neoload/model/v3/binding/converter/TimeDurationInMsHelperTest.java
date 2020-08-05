package com.neotys.neoload.model.v3.binding.converter;

import org.junit.Test;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsHelper.convertToDuration;
import static com.neotys.neoload.model.v3.binding.converter.TimeDurationInMsHelper.convertToString;
import static org.junit.Assert.assertEquals;

public class TimeDurationInMsHelperTest {

	@Test
	public void testConvertToString() {
		assertEquals("0", convertToString(null));
		assertEquals("0", convertToString(-1000l));	
		assertEquals("0", convertToString(0l));
		
		assertEquals("1h", convertToString(3600000l));
		assertEquals("2h 3m 4s 5ms", convertToString(7384005l));
		assertEquals("3m 5ms", convertToString(180005l));
		assertEquals("5ms", convertToString(5l));
	}

	@Test
	public void testConvertToLong() {
		assertEquals("0", convertToDuration(null));
		assertEquals("0", convertToDuration("-1000"));
		assertEquals("0", convertToDuration(""));
		
		assertEquals("3600000", convertToDuration("1h 	"));
		assertEquals("7384005", convertToDuration("2h	3m		4s 5ms"));
		assertEquals("7384005", convertToDuration("2h3m4s5ms"));
		assertEquals("180005", convertToDuration("3m    5ms"));
		assertEquals("5", convertToDuration("5ms"));
		assertEquals("1000", convertToDuration("1000"));
	}
}