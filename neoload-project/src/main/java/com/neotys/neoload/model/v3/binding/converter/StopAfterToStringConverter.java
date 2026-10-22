package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;

public final class StopAfterToStringConverter extends StdConverter<StopAfter, String> {
	@Override
	public String convert(final StopAfter stopAfter) {
		if (stopAfter == null) return null;
		
		if (stopAfter.getInvalidText().isPresent()) {
			return stopAfter.getInvalidText().get();
		}
		String convertedValue;
		final StopAfter.Type type = stopAfter.getType();
		if (type == StopAfter.Type.TIME) {
			convertedValue = Durations.format((Integer) stopAfter.getValue(), DurationGrammar.SECONDS);
		}
		else {
			convertedValue = "current_iteration";
		}
		return convertedValue;
	}
}
