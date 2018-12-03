package com.neotys.neoload.model.v3.binding.converter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;

import org.junit.Test;


public class PercentageToDoubleConverterTest {
	@Test
	public void shouldConvertCorrectly() {
		final PercentageToDoubleConverter converter = new PercentageToDoubleConverter();
		
		assertEquals(Optional.empty(), converter.convert(null));

		boolean throwException = false;
		try {
			converter.convert("string");
		}
		catch (final Exception e) {
			assertTrue(e.getMessage().contains("The value 'string' is not a valid percentage."));
			throwException = true;
		}
		if (!throwException) {
			fail("The value 'string' is not a valid percentage.");
		}

		assertEquals(Optional.ofNullable(Double.valueOf(50)), converter.convert("50"));
		assertEquals(Optional.ofNullable(Double.valueOf(50)), converter.convert("50%"));

		assertEquals(Optional.ofNullable(Double.valueOf(1000)), converter.convert("1000"));
		assertEquals(Optional.ofNullable(Double.valueOf(1000)), converter.convert("1000%"));
	}
}
