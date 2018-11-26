package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;


public final class PercentageToDoubleConverter extends StdConverter<String, Double> {
	@Override
	public Double convert(final String input) {
		return PercentageHelper.convertToDouble(input);
	}
}
