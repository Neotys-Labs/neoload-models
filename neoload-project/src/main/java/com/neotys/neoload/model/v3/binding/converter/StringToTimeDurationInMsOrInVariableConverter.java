package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Normalizes a millisecond duration text to a plain number of milliseconds, or keeps a variable reference.
 * A text that is not a valid duration is kept as written so that validation reports it.
 */
public final class StringToTimeDurationInMsOrInVariableConverter extends StdConverter<String, String> {
	public static final StringToTimeDurationInMsOrInVariableConverter STRING_TO_TIME_DURATION_IN_MS_OR_IN_VARIABLE = new StringToTimeDurationInMsOrInVariableConverter();

	private StringToTimeDurationInMsOrInVariableConverter() {
		//do nothing
	}

	@Override
	public String convert(final String rawInput) {
		try {
			final ParsedDuration parsed = Durations.parse(rawInput, DurationGrammar.MILLISECONDS);
			return parsed.isVariable() ? "${" + parsed.getVariableName() + "}" : String.valueOf(parsed.getAmount());
		} catch (final InvalidDurationException e) {
			return rawInput;
		}
	}
}
