package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;

import static com.google.common.base.Strings.isNullOrEmpty;

public final class StringToTimeDurationWithMsConverter extends StdConverter<String, String> {
	private static final String ERROR_VALUE = "0";

	public static final StringToTimeDurationWithMsConverter STRING_TO_TIME_DURATION_WITH_MS = new StringToTimeDurationWithMsConverter();

	private StringToTimeDurationWithMsConverter() {
		//do nothing
	}

	@Override
	public String convert(final String input) {
		if (isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}

		if (input.startsWith("${") && input.endsWith("}")) {
			//variable
			return input;
		}

		return TimeDurationWithMsHelper.convertToDuration(input);
	}
}
