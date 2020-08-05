package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import static com.google.common.base.Strings.isNullOrEmpty;

public final class StringToTimeDurationInMsOrInVariableConverter extends StdConverter<String, String> {
	private static final String ERROR_VALUE = "0";

	public static final StringToTimeDurationInMsOrInVariableConverter STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE = new StringToTimeDurationInMsOrInVariableConverter();

	private StringToTimeDurationInMsOrInVariableConverter() {
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

		return TimeDurationInMsHelper.convertToDuration(input);
	}
}
