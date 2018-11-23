package com.neotys.neoload.model.converter;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class PercentageToDoubleConverterTest {
	@Test
	public void shouldConvertCorrectly() {
		final PercentageToDoubleConverter converter = new PercentageToDoubleConverter();
		
		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));
		// Input: NULL - Output: 0.0.0%
		assertEquals(null, converter.convert("0.0.0%"));
		// Input: NULL - Output: 0,0,0%
		assertEquals(null, converter.convert("0,0,0%"));
		// Input: NULL - Output: 0,0,0%
		assertEquals(null, converter.convert("0,0.0%"));
		
		// Percentage (integer)
		// Input: -50 - Output: -50%
		assertEquals(Double.valueOf(-50), converter.convert("-50%"));
		// Input: 0 - Output: 0%
		assertEquals(Double.valueOf(0), converter.convert("0%"));
		// Input: 0 - Output: -0%
		assertEquals(Double.valueOf("-0"), converter.convert("-0%"));
		// Input: 50 - Output: 50%
		assertEquals(Double.valueOf(50), converter.convert("50%"));
		
		// Percentage (double)
		// Input: -50.8% - Output: -50.8
		assertEquals(Double.valueOf(-50.8), converter.convert("-50.8%"));
		// Input: 50.4% - Output: 50.4
		assertEquals(Double.valueOf(50.4), converter.convert("50.4%"));
		
		// Input: 50.46% - Output: 50.46
		assertEquals(Double.valueOf(50.46), converter.convert("50.46%"));
		
		// Input: -50.455% - Output: -50.4554
		assertEquals(Double.valueOf(-50.455), converter.convert("-50.4554%"));
		// Input: 50.455% - Output: 50.4554
		assertEquals(Double.valueOf(50.455), converter.convert("50.4554%"));
		// Input: -50.456% - Output: -50.4555
		assertEquals(Double.valueOf(-50.456), converter.convert("-50.4555%"));
		// Input: 50.456% - Output: 50.4555
		assertEquals(Double.valueOf(50.456), converter.convert("50.4555%"));
		// Input: -50.456% - Output: -50.4556
		assertEquals(Double.valueOf(-50.456), converter.convert("-50.4556%"));
		// Input: 50.456% - Output: 50.4556
		assertEquals(Double.valueOf(50.456), converter.convert("50.4556%"));

		// Input: -1000% - Output: -1000
		assertEquals(Double.valueOf(-1000), converter.convert("-1000%"));
		// Input: 1000% - Output: 1000
		assertEquals(Double.valueOf(1000), converter.convert("1000%"));
	}
	
	private void check(final String separator, final String unit) {
	}
}
