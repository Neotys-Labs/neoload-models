package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

public final class TimeDurationToStringConverter extends StdConverter<Integer, String> {
	@Override
	public String convert(final Integer input) {
		if (input != null) {
			return TimeDurationHelper.convertToString(input);
		}
		return null;
	}
}
