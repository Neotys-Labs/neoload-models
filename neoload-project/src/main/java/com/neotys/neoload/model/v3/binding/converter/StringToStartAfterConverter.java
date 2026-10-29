package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

/**
 * A number of seconds or a valid duration text is a time delay, a negative or oversized number failing the read;
 * any other text is a population name.
 */
public final class StringToStartAfterConverter extends StdConverter<String, StartAfter> {
	private static final StartAfter ERROR_VALUE = StartAfter.builder().build();
	private static final Pattern NUMBER = Pattern.compile("\\s*-?\\d+\\s*");

	@Override
	public StartAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		if (NUMBER.matcher(input).matches()) {
			return time(input);
		}
		try {
			return time(input);
		} catch (final InvalidDurationException e) {
			return StartAfter.builder()
					.value(input)
					.type(StartAfter.Type.POPULATION)
					.build();
		}
	}

	private static StartAfter time(final String input) {
		return StartAfter.builder()
				.value(DurationParser.parse(input, ChronoUnit.SECONDS))
				.type(StartAfter.Type.TIME)
				.build();
	}
}
