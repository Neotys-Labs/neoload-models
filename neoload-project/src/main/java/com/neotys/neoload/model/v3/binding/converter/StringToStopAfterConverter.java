package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import java.util.Optional;

public final class StringToStopAfterConverter extends StdConverter<String, StopAfter> {
	private static final StopAfter ERROR_VALUE = StopAfter.builder().build();

	@Override
	public StopAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		// Time case
		final Optional<Integer> seconds = Durations.tryParseSeconds(input);
		if (seconds.isPresent()) {
			return StopAfter.builder()
					.value(seconds.get())
					.type(StopAfter.Type.TIME)
					.build();		
		}
		// Current Iteration case
		if ("current_iteration".equals(input.trim())) {
			return StopAfter.builder()
					.type(StopAfter.Type.CURRENT_ITERATION)
					.build();
		}
		return StopAfter.builder().invalidText(input).build();
	}
}
