package com.neotys.neoload.model.v3.binding.converter;

import java.util.Optional;

import com.fasterxml.jackson.databind.util.StdConverter;


public final class PercentageToDoubleConverter extends StdConverter<String, Optional<Double>> {
	@Override
	public Optional<Double> convert(final String input) {
		return Optional.ofNullable(PercentageHelper.convertToDouble(input));
	}
}
