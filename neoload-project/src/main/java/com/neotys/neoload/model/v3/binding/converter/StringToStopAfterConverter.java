package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import java.time.temporal.ChronoUnit;

/**
 * A number of seconds or a valid duration text is a time delay and {@code current_iteration} the end of the current iteration;
 * any other text fails the read.
 */
public final class StringToStopAfterConverter extends StdConverter<String, StopAfter> {
	private static final StopAfter ERROR_VALUE = StopAfter.builder().build();
	private static final String CURRENT_ITERATION = "current_iteration";

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
				.value(DurationParser.parse(input, ChronoUnit.SECONDS))
				.type(StopAfter.Type.TIME)
				.build();
	}
}
