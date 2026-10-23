package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import java.util.regex.Pattern;

/**
 * Reads {@code start_after}: a positive delay in seconds, or the name of the population to start after.
 * Text starting with a negative number is read as an invalid delay, not as a population name.
 */
public final class StringToStartAfterConverter extends StdConverter<String, StartAfter> {
	private static final StartAfter ERROR_VALUE = StartAfter.builder().build();
	private static final Pattern NEGATIVE_NUMBER = Pattern.compile("\\s*-\\d");

	@Override
	public StartAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}

		final SecondsDuration time = SecondsDuration.parse(input);
		if (!time.isInvalid() || NEGATIVE_NUMBER.matcher(input).lookingAt()) {
			return StartAfter.builder()
					.value(time)
					.type(StartAfter.Type.TIME)
					.build();
		}
		return StartAfter.builder()
				.value(input)
				.type(StartAfter.Type.POPULATION)
				.build();
	}
}
