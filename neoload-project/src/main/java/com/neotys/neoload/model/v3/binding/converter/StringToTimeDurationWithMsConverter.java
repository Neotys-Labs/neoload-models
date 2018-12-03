package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;

public final class StringToTimeDurationWithMsConverter extends StdConverter<String, Long> {
	private static final Long ERROR_VALUE = 0L;

	public static final StringToTimeDurationWithMsConverter STRING_TO_TIME_DURATION_WITH_MS = new StringToTimeDurationWithMsConverter();

	private StringToTimeDurationWithMsConverter() {
		//do nothing
	}

	@Override
	public Long convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}

		return TimeDurationWithMsHelper.convertToLong(input);
	}
}
