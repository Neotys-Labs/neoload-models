package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.scenario.ImmutableStopAfter;
import com.neotys.neoload.model.scenario.StopAfter;
import com.neotys.neoload.model.scenario.StopAfter.Type;

public final class StringToStopAfterConverter extends StdConverter<String, StopAfter> {
	private static final StopAfter ERROR_VALUE = null;

	@Override
	public StopAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		// Time case
		final Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return ImmutableStopAfter.builder()
					.value(value)
					.type(Type.TIME)
					.build();		
		}
		// Current Iteration case
		if ("current_iteration".equals(input.trim())) {
			return ImmutableStopAfter.builder()
					.type(Type.CURRENT_ITERATION)
					.build();
		}
		return ERROR_VALUE;
	}
}
