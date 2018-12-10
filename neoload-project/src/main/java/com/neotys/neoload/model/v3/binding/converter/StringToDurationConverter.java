package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;

public final class StringToDurationConverter extends StdConverter<String, LoadDuration> {
	private static final LoadDuration ERROR_VALUE = LoadDuration.builder().build();

	@Override
	public LoadDuration convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return LoadDuration.builder()
					.value(value)
					.type(LoadDuration.Type.TIME)
					.build();		
		}
		value = IterationDurationHelper.convertToInteger(input);
		if (value != null) {
			return LoadDuration.builder()
					.value(value)
					.type(LoadDuration.Type.ITERATION)
					.build();
		}
		return ERROR_VALUE;
	}
}
