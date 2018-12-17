package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.scenario.Duration;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public final class StringToDurationConverter extends StdConverter<String, Duration> {
	private static final Duration ERROR_VALUE = Duration.builder().build();

	@Override
	public Duration convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return Duration.builder()
					.value(value)
					.type(Duration.Type.TIME)
					.build();		
		}
		value = IterationDurationHelper.convertToInteger(input);
		if (value != null) {
			return Duration.builder()
					.value(value)
					.type(Duration.Type.ITERATION)
					.build();
		}
		return ERROR_VALUE;
	}
}
