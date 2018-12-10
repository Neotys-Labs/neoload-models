package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.scenario.StopAfter;

@Deprecated
public final class StringToStopAfterConverter extends StdConverter<String, StopAfter> {
	private static final StopAfter ERROR_VALUE = StopAfter.builder().build();

	@Override
	public StopAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		// Time case
		final Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return StopAfter.builder()
					.value(value)
					.type(StopAfter.Type.TIME)
					.build();		
		}
		// Current Iteration case
		if ("current_iteration".equals(input.trim())) {
			return StopAfter.builder()
					.type(StopAfter.Type.CURRENT_ITERATION)
					.build();
		}
		return ERROR_VALUE;
	}
}
