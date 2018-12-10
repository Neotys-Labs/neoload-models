package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.scenario.Duration;

@Deprecated
public final class DurationToStringConverter extends StdConverter<Duration, String> {
	@Override
	public String convert(final Duration duration) {
		if (duration == null) return null;
		
		String convertedValue = null;
		final Duration.Type type = duration.getType();
		if (type == Duration.Type.TIME) { 
			convertedValue = TimeDurationHelper.convertToString(duration.getValue());
		}
		else {
			convertedValue = IterationDurationHelper.convertToString(duration.getValue());
		}
		return convertedValue;
	}
}
