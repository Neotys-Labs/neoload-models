package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;

public final class StringToDurationConverter extends StdConverter<String, LoadDuration> {
	private static final LoadDuration ERROR_VALUE = LoadDuration.builder().build();

	@Override
	public LoadDuration convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}

		final Integer iterations = IterationDurationHelper.convertToInteger(input);
		if (iterations != null) {
			return LoadDuration.builder()
					.value(iterations)
					.type(LoadDuration.Type.ITERATION)
					.build();
		}
		return LoadDuration.builder()
				.value(SecondsDuration.parse(input))
				.type(LoadDuration.Type.TIME)
				.build();
	}
}
