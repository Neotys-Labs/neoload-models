package com.neotys.neoload.model.v3.binding.converter;

import org.junit.Test;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationWithMsHelper.convertToDuration;
import static com.neotys.neoload.model.v3.binding.converter.TimeDurationWithMsHelper.convertToString;
import static org.junit.Assert.assertEquals;

public class TimeDurationWithMsHelperTest {

	@Test
	public void testConvertToString() {
		assertEquals("1h", convertToString(3600000));
		assertEquals("2h3m4s5ms", convertToString(7384005));
		assertEquals("3m5ms", convertToString(180005));
		assertEquals("5ms", convertToString(5));
	}

	@Test
	public void testConvertToLong() {
		assertEquals("3600000", convertToDuration("1h 	"));
		assertEquals("7384005", convertToDuration("2h3m		4s 5ms"));
		assertEquals("180005", convertToDuration("3m    5ms"));
		assertEquals("5", convertToDuration("5ms"));
		assertEquals("1000", convertToDuration("1000"));
	}
}