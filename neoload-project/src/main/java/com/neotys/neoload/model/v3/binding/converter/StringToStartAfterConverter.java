package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

/**
 * A text starting with a digit, or a sign followed by a digit, is a time delay in seconds: an invalid duration
 * fails the read instead of being taken for a population name. Any other text is a population name.
 */
public final class StringToStartAfterConverter extends StdConverter<String, StartAfter> {
	private static final StartAfter ERROR_VALUE = StartAfter.builder().build();
	private static final Pattern DURATION_LIKE = Pattern.compile("\\s*-?\\d.*", Pattern.DOTALL);

	@Override
	public StartAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		if (DURATION_LIKE.matcher(input).matches()) {
			return StartAfter.builder()
					.value(DurationParser.parse(input, ChronoUnit.SECONDS))
					.type(StartAfter.Type.TIME)
					.build();
		}
		return StartAfter.builder()
				.value(input)
				.type(StartAfter.Type.POPULATION)
				.build();
	}
}
