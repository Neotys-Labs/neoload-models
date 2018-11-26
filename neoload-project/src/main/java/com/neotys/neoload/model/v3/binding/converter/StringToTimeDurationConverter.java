package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;

public final class StringToTimeDurationConverter extends StdConverter<String, Integer> {
	private static final Integer ERROR_VALUE = Integer.MIN_VALUE;

	@Override
	public Integer convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		final Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return value;
		}
		return ERROR_VALUE;
	}
}
