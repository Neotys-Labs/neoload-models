package com.neotys.neoload.model.v3.binding.converter;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Formats a stored number of milliseconds compactly, and keeps a variable reference or an invalid text as written.
 */
public final class TimeDurationInMsOrInVariableToStringConverter extends StdConverter<String, String> {
	private static final String ZERO = "0";

	public static final TimeDurationInMsOrInVariableToStringConverter TIME_DURATION_IN_MS_OR_IN_VARIABLE_TO_STRING = new TimeDurationInMsOrInVariableToStringConverter();

	private TimeDurationInMsOrInVariableToStringConverter() {
		//do nothing
	}

	@Override
	public String convert(final String input) {
		if (isNullOrEmpty(input)) {
			return ZERO;
		}
		try {
			final ParsedDuration parsed = Durations.parse(input, DurationGrammar.MILLISECONDS);
			return parsed.isVariable() ? input : Durations.format(parsed.getAmount(), DurationGrammar.MILLISECONDS);
		} catch (final InvalidDurationException e) {
			return input;
		}
	}
}
