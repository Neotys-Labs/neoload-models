package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;

public final class DurationToStringConverter extends StdConverter<LoadDuration, String> {
	@Override
	public String convert(final LoadDuration duration) {
		if (duration == null) return null;
		
		String convertedValue;
		final LoadDuration.Type type = duration.getType();
		if (type == LoadDuration.Type.TIME) { 
			convertedValue = TimeDurationHelper.convertToString(duration.getValue());
		}
		else {
			convertedValue = IterationDurationHelper.convertToString(duration.getValue());
		}
		return convertedValue;
	}
}
