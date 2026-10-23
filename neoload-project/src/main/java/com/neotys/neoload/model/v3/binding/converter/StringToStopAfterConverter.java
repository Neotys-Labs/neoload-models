package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;

public final class StringToStopAfterConverter extends StdConverter<String, StopAfter> {
	private static final String CURRENT_ITERATION = "current_iteration";
	private static final StopAfter ERROR_VALUE = StopAfter.builder().build();

	@Override
	public StopAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}

		if (CURRENT_ITERATION.equals(input.trim())) {
			return StopAfter.builder()
					.type(StopAfter.Type.CURRENT_ITERATION)
					.build();
		}
		return StopAfter.builder()
				.value(SecondsDuration.parse(input))
				.type(StopAfter.Type.TIME)
				.build();
	}
}
