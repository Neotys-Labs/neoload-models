package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.scenario.ImmutableStartAfter;
import com.neotys.neoload.model.scenario.StartAfter;
import com.neotys.neoload.model.scenario.StartAfter.Type;

public final class StringToStartAfterConverter extends StdConverter<String, StartAfter> {
	private static final StartAfter ERROR_VALUE = null;

	@Override
	public StartAfter convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		try {
			// Time case
			final Integer value = TimeDurationHelper.convertToInteger(input);
			if (value != null) {
				return ImmutableStartAfter.builder()
						.value(value)
						.type(Type.TIME)
						.build();		
			}
			// Population case
			return ImmutableStartAfter.builder()
					.value(input)
					.type(Type.POPULATION)
					.build();
		}
		catch(final Throwable exception) {
			return ERROR_VALUE;
		}		
	}
}
