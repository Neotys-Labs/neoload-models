package com.neotys.neoload.model.v3.binding.converter;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.binding.converter.DoubleToPercentageConverter;
import com.neotys.neoload.model.v3.project.scenario.Duration;
import com.neotys.neoload.model.v3.project.scenario.Duration.Type;


public class DoubleToPercentageConverterTest {
	@Test
	public void shouldConvertCorrectly() {
		final DoubleToPercentageConverter converter = new DoubleToPercentageConverter();

		// Input: NULL - Output: NULL
		assertEquals(null, converter.convert(null));

		// Percentage (integer)
		// Input: -50% - Output: -50
		assertEquals("-50%", converter.convert(Double.valueOf(-50)));
		// Input: 0 - Output: 0
		assertEquals("0%", converter.convert(Double.valueOf(0)));
		// Input: 50% - Output: 50
		assertEquals("50%", converter.convert(Double.valueOf(50)));
		
		// Percentage (double)
		// Input: -50.8% - Output: -50.8
		assertEquals("-50.8%", converter.convert(Double.valueOf(-50.8)));
		// Input: 50.4% - Output: 50.4
		assertEquals("50.4%", converter.convert(Double.valueOf(50.4)));
		
		// Input: -50.455% - Output: -50.4554
		assertEquals("-50.455%", converter.convert(Double.valueOf(-50.4554)));
		// Input: 50.455% - Output: 50.4554
		assertEquals("50.455%", converter.convert(Double.valueOf(50.4554)));
		// Input: -50.456% - Output: -50.4555
		assertEquals("-50.456%", converter.convert(Double.valueOf(-50.4555)));
		// Input: 50.456% - Output: 50.4555
		assertEquals("50.456%", converter.convert(Double.valueOf(50.4555)));
		// Input: -50.456% - Output: -50.4556
		assertEquals("-50.456%", converter.convert(Double.valueOf(-50.4556)));
		// Input: 50.456% - Output: 50.4556
		assertEquals("50.456%", converter.convert(Double.valueOf(50.4556)));

		// Input: -1000% - Output: -1000
		assertEquals("-1000%", converter.convert(Double.valueOf(-1000)));
		// Input: 1000% - Output: 1000
		assertEquals("1000%", converter.convert(Double.valueOf(1000)));
	}
}
