package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.scenario.StartAfter;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public final class StringToStartAfterConverter extends StdConverter<String, StartAfter> {
	private static final StartAfter ERROR_VALUE = StartAfter.builder().build();

	@Override
	public StartAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		// Time case
		final Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return StartAfter.builder()
					.value(value)
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
