package com.neotys.neoload.model.converter;

import java.util.Optional;

import com.fasterxml.jackson.databind.util.StdConverter;

public final class TimeDurationToStringConverter extends StdConverter<Optional<Integer>, String> {
	@Override
	public String convert(final Optional<Integer> input) {
		if (input.isPresent()) {
			return TimeDurationHelper.convertToString(input.get());
		}
		return null;
	}
}
