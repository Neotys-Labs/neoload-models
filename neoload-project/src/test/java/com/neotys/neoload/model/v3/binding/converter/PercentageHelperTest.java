package com.neotys.neoload.model.v3.binding.converter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;


public class PercentageHelperTest {
	@Test
	public void convertToString() {
		// Input: NULL - Output: NULL
		assertEquals(null, PercentageHelper.convertToString(null));

		// Percentage (integer)
		// Input: -50% - Output: -50
		assertEquals("-50%", PercentageHelper.convertToString(Double.valueOf(-50)));
		// Input: 0 - Output: 0
		assertEquals("0%", PercentageHelper.convertToString(Double.valueOf(0)));
		// Input: 50% - Output: 50
		assertEquals("50%", PercentageHelper.convertToString(Double.valueOf(50)));
		
		// Percentage (double)
		// Input: -50.8% - Output: -50.8
		assertEquals("-50.8%", PercentageHelper.convertToString(Double.valueOf(-50.8)));
		// Input: 50.4% - Output: 50.4
		assertEquals("50.4%", PercentageHelper.convertToString(Double.valueOf(50.4)));
		
		// Input: -50.455% - Output: -50.4554
		assertEquals("-50.455%", PercentageHelper.convertToString(Double.valueOf(-50.4554)));
		// Input: 50.455% - Output: 50.4554
		assertEquals("50.455%", PercentageHelper.convertToString(Double.valueOf(50.4554)));
		// Input: -50.456% - Output: -50.4555
		assertEquals("-50.456%", PercentageHelper.convertToString(Double.valueOf(-50.4555)));
		// Input: 50.456% - Output: 50.4555
		assertEquals("50.456%", PercentageHelper.convertToString(Double.valueOf(50.4555)));
		// Input: -50.456% - Output: -50.4556
		assertEquals("-50.456%", PercentageHelper.convertToString(Double.valueOf(-50.4556)));
		// Input: 50.456% - Output: 50.4556
		assertEquals("50.456%", PercentageHelper.convertToString(Double.valueOf(50.4556)));

		// Input: -1000% - Output: -1000
		assertEquals("-1000%", PercentageHelper.convertToString(Double.valueOf(-1000)));
		// Input: 1000% - Output: 1000
		assertEquals("1000%", PercentageHelper.convertToString(Double.valueOf(1000)));
	}
	
	@Test
	public void convertToDouble() {
		// Input: NULL - Output: NULL
		assertEquals(null, PercentageHelper.convertToDouble(null));
		
		convertToDouble("");
		convertToDouble("%");
	}
	
	private void convertToDouble(final String unit) {
		// Input: NULL - Output: NULL
		assertEquals(null, PercentageHelper.convertToDouble(null));
		// Input: NULL - Output: 0.0.0unit
		boolean throwException = false;
		try {
			PercentageHelper.convertToDouble("str"+unit);
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
			PercentageHelper.convertToDouble("0.0.0"+unit);
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
			PercentageHelper.convertToDouble("0,0,0"+unit);
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
			PercentageHelper.convertToDouble("0,0.0"+unit);
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
		assertEquals(Double.valueOf(-50), PercentageHelper.convertToDouble("-50"+unit));
		// Input: 0 - Output: 0unit
		assertEquals(Double.valueOf(0), PercentageHelper.convertToDouble("0"+unit));
		// Input: 0 - Output: -0unit
		assertEquals(Double.valueOf("-0"), PercentageHelper.convertToDouble("-0"+unit));
		// Input: 50 - Output: 50unit
		assertEquals(Double.valueOf(50), PercentageHelper.convertToDouble("50"+unit));
		
		// Percentage (double)
		// Input: -50.8 - Output: -50.8unit
		assertEquals(Double.valueOf(-50.8), PercentageHelper.convertToDouble("-50.8"+unit));
		// Input: 50.4 - Output: 50.4unit
		assertEquals(Double.valueOf(50.4), PercentageHelper.convertToDouble("50.4"+unit));
		
		// Input: 50.46 - Output: 50.46unit
		assertEquals(Double.valueOf(50.46), PercentageHelper.convertToDouble("50.46"+unit));
		
		// Input: -50.455 - Output: -50.4554unit
		assertEquals(Double.valueOf(-50.455), PercentageHelper.convertToDouble("-50.4554"+unit));
		// Input: 50.455 - Output: 50.4554unit
		assertEquals(Double.valueOf(50.455), PercentageHelper.convertToDouble("50.4554"+unit));
		// Input: -50.456 - Output: -50.4555unit
		assertEquals(Double.valueOf(-50.456), PercentageHelper.convertToDouble("-50.4555"+unit));
		// Input: 50.456 - Output: 50.4555unit
		assertEquals(Double.valueOf(50.456), PercentageHelper.convertToDouble("50.4555"+unit));
		// Input: -50.456 - Output: -50.4556unit
		assertEquals(Double.valueOf(-50.456), PercentageHelper.convertToDouble("-50.4556"+unit));
		// Input: 50.456 - Output: 50.4556unit
		assertEquals(Double.valueOf(50.456), PercentageHelper.convertToDouble("50.4556"+unit));

		// Input: -1000 - Output: -1000unit
		assertEquals(Double.valueOf(-1000), PercentageHelper.convertToDouble("-1000"+unit));
		// Input: 1000 - Output: 1000unit
		assertEquals(Double.valueOf(1000), PercentageHelper.convertToDouble("1000"+unit));
	}
}
