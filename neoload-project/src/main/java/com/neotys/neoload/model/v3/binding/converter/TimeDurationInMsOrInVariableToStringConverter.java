package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import static com.google.common.base.Strings.isNullOrEmpty;

public final class TimeDurationInMsOrInVariableToStringConverter extends StdConverter<String, String> {
	private static final String ERROR_VALUE = "0";

	public static final TimeDurationInMsOrInVariableToStringConverter TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING = new TimeDurationInMsOrInVariableToStringConverter();

	private TimeDurationInMsOrInVariableToStringConverter() {
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

		return TimeDurationInMsHelper.convertToString(Long.valueOf(input));
	}
}
