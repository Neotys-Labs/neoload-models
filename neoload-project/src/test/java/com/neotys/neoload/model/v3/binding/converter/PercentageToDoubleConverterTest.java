package com.neotys.neoload.model.v3.binding.converter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;


public class PercentageToDoubleConverterTest {
	@Test
	public void shouldConvertCorrectly() {
		shouldConvertCorrectly("");
		shouldConvertCorrectly("%");
	}
	
	private void shouldConvertCorrectly(final String unit) {
		final PercentageToDoubleConverter converter = new PercentageToDoubleConverter();
		
		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));
		// Input: NULL - Output: 0.0.0unit
		boolean throwException = false;
		try {
			assertEquals(null, converter.convert("str"+unit));
		}
		catch (final Exception e) {
			assertTrue(e.getMessage().contains("The value 'str"+unit+"' is not a valid percentage."));
			throwException = true;
		}
		if (!throwException) {
			fail("The value 'str"+unit+"' is not a valid percentage.");
		}
		throwException = false;
		try {
			assertEquals(null, converter.convert("0.0.0"+unit));
		}
		catch (final Exception e) {
			assertTrue(e.getMessage().contains("The value '0.0.0"+unit+"' is not a valid percentage."));
			throwException = true;
		}
		if (!throwException) {
			fail("The value '0.0.0"+unit+"' is not a valid percentage.");
		}
		// Input: NULL - Output: 0,0,0unit
		throwException = false;
		try {
			assertEquals(null, converter.convert("0,0,0"+unit));
		}
		catch (final Exception e) {
			assertTrue(e.getMessage().contains("The value '0,0,0"+unit+"' is not a valid percentage."));
			throwException = true;
		}
		if (!throwException) {
			fail("The value '0,0,0"+unit+"' is not a valid percentage.");
		}
		// Input: NULL - Output: 0,0.0unit
		throwException = false;
		try {
			assertEquals(null, converter.convert("0,0.0"+unit));
		}
		catch (final Exception e) {
			assertTrue(e.getMessage().contains("The value '0,0.0"+unit+"' is not a valid percentage."));
			throwException = true;
		}
		if (!throwException) {
			fail("The value '0,0.0"+unit+"' is not a valid percentage.");
		}
		
		// Percentage (integer)
		// Input: -50 - Output: -50unit
		assertEquals(Double.valueOf(-50), converter.convert("-50"+unit));
		// Input: 0 - Output: 0unit
		assertEquals(Double.valueOf(0), converter.convert("0"+unit));
		// Input: 0 - Output: -0unit
		assertEquals(Double.valueOf("-0"), converter.convert("-0"+unit));
		// Input: 50 - Output: 50unit
		assertEquals(Double.valueOf(50), converter.convert("50"+unit));
		
		// Percentage (double)
		// Input: -50.8 - Output: -50.8unit
		assertEquals(Double.valueOf(-50.8), converter.convert("-50.8"+unit));
		// Input: 50.4 - Output: 50.4unit
		assertEquals(Double.valueOf(50.4), converter.convert("50.4"+unit));
		
		// Input: 50.46 - Output: 50.46unit
		assertEquals(Double.valueOf(50.46), converter.convert("50.46"+unit));
		
		// Input: -50.455 - Output: -50.4554unit
		assertEquals(Double.valueOf(-50.455), converter.convert("-50.4554"+unit));
		// Input: 50.455 - Output: 50.4554unit
		assertEquals(Double.valueOf(50.455), converter.convert("50.4554"+unit));
		// Input: -50.456 - Output: -50.4555unit
		assertEquals(Double.valueOf(-50.456), converter.convert("-50.4555"+unit));
		// Input: 50.456 - Output: 50.4555unit
		assertEquals(Double.valueOf(50.456), converter.convert("50.4555"+unit));
		// Input: -50.456 - Output: -50.4556unit
		assertEquals(Double.valueOf(-50.456), converter.convert("-50.4556"+unit));
		// Input: 50.456 - Output: 50.4556unit
		assertEquals(Double.valueOf(50.456), converter.convert("50.4556"+unit));

		// Input: -1000 - Output: -1000unit
		assertEquals(Double.valueOf(-1000), converter.convert("-1000"+unit));
		// Input: 1000 - Output: 1000unit
		assertEquals(Double.valueOf(1000), converter.convert("1000"+unit));

	}
}
