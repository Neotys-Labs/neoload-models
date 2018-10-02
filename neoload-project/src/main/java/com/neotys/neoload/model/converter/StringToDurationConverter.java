package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.scenario.ImmutableDuration;
import com.neotys.neoload.model.scenario.Duration.Type;

public final class StringToDurationConverter extends StdConverter<String, Duration> {
	private static final Duration ERROR_VALUE = ImmutableDuration.builder()
			.value(-1)
			.type(Type.TIME)
			.build();

	@Override
	public Duration convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return ImmutableDuration.builder()
					.value(value)
					.type(Type.TIME)
					.build();		
		}
		value = IterationDurationHelper.convertToInteger(input);
		if (value != null) {
			return ImmutableDuration.builder()
					.value(value)
					.type(Type.ITERATION)
					.build();
		}
		return ERROR_VALUE;
	}
}
