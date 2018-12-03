package com.neotys.neoload.model.v3.binding.converter;

import org.junit.Test;

import static com.neotys.neoload.model.v3.binding.converter.TimeDurationWithMsHelper.convertToLong;
import static com.neotys.neoload.model.v3.binding.converter.TimeDurationWithMsHelper.convertToString;
import static org.junit.Assert.*;

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
		assertEquals(3600000, convertToLong("1h 	").longValue());
		assertEquals(7384005, convertToLong("2h3m		4s 5ms").longValue());
		assertEquals(180005, convertToLong("3m    5ms").longValue());
		assertEquals(5, convertToLong("5ms").longValue());
	}
}