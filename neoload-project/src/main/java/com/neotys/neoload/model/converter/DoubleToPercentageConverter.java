package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

public final class DoubleToPercentageConverter extends StdConverter<Double, String> {
	@Override
	public String convert(final Double value) {
		return PercentageHelper.convertToString(value);
	}
}
