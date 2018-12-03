package com.neotys.neoload.model.v3.binding.converter;

import java.util.Optional;

import com.fasterxml.jackson.databind.util.StdConverter;

public final class DoubleToPercentageConverter extends StdConverter<Optional<Double>, String> {
	@Override
	public String convert(final Optional<Double> input) {
		return PercentageHelper.convertToString(input.orElse(null));
	}
}
