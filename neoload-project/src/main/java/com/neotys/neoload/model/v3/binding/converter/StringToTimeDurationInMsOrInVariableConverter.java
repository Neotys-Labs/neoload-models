package com.neotys.neoload.model.v3.binding.converter;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.fasterxml.jackson.databind.util.StdConverter;

public final class StringToTimeDurationInMsOrInVariableConverter extends StdConverter<String, String> {
	public static final StringToTimeDurationInMsOrInVariableConverter STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE = new StringToTimeDurationInMsOrInVariableConverter();

	private StringToTimeDurationInMsOrInVariableConverter() {
		//do nothing
	}

	/**
	 * @throws InvalidDurationException if the input is neither a valid duration nor a variable
	 */
	@Override
	public String convert(final String rawInput) {
		if (isNullOrEmpty(rawInput)) {
			throw new InvalidDurationException(rawInput);
		}

		final String input = rawInput.trim();
		if (input.startsWith("${") && input.endsWith("}")) {
			//variable
			return input;
		}

		return TimeDurationInMsHelper.convertToDuration(input);
	}
}
