package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import java.time.Duration;

public final class StopAfterToStringConverter extends StdConverter<StopAfter, String> {
	@Override
	public String convert(final StopAfter stopAfter) {
		if (stopAfter == null) return null;
		
		String convertedValue;
		final StopAfter.Type type = stopAfter.getType();
		if (type == StopAfter.Type.TIME) {
			convertedValue = DurationParser.format((Duration) stopAfter.getValue());
		}
		else {
			convertedValue = "current_iteration";
		}
		return convertedValue;
	}
}
