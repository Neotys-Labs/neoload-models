package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.scenario.Duration;

public final class DurationToStringConverter extends StdConverter<Duration, String> {
	@Override
	public String convert(final Duration duration) {
		if (duration == null) return null;
		
		String convertedValue = null;
		final Duration.Type type = duration.getType();
		switch (type) {
			case TIME: 
				convertedValue = TimeDurationHelper.convertToString(duration.getValue());
				break;
			case ITERATION: 
				convertedValue = IterationDurationHelper.convertToString(duration.getValue());
				break;
		}
		return convertedValue;
	}
}
