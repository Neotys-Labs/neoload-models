package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import java.util.Optional;

public final class StringToStartAfterConverter extends StdConverter<String, StartAfter> {
	private static final StartAfter ERROR_VALUE = StartAfter.builder().build();

	@Override
	public StartAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		// Time case
		final Optional<Integer> seconds = Durations.tryParseSeconds(input);
		if (seconds.isPresent()) {
			return StartAfter.builder()
					.value(seconds.get())
					.type(StartAfter.Type.TIME)
					.build();		
		}
		// Population case
		return StartAfter.builder()
				.value(input)
				.type(StartAfter.Type.POPULATION)
				.build();
	}
}
