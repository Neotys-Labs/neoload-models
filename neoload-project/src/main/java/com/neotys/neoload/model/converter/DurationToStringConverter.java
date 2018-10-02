package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.scenario.Duration;

public final class DurationToStringConverter extends StdConverter<Duration, String> {
	@Override
	public String convert(final Duration duration) {
		if (duration == null) return null;
		
		final Duration.Type type = duration.getType();
		switch (type) {
			case TIME: 
				return TimeDurationHelper.convertToString(duration.getValue());
			case ITERATION: 
				return IterationDurationHelper.convertToString(duration.getValue());
			default:
				return null;
		}
	}
}
