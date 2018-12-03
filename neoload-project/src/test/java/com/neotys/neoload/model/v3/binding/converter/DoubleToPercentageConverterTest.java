package com.neotys.neoload.model.v3.binding.converter;


import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;


public class DoubleToPercentageConverterTest {
	@Test
	public void shouldConvertCorrectly() {
		final DoubleToPercentageConverter converter = new DoubleToPercentageConverter();

		assertEquals(null, converter.convert(Optional.empty()));

		assertEquals("-50%", converter.convert(Optional.ofNullable(Double.valueOf(-50))));
		assertEquals("1000%", converter.convert(Optional.ofNullable(Double.valueOf(1000))));
	}
}
