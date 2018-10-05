package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.scenario.StopAfter;

public final class StopAfterToStringConverter extends StdConverter<StopAfter, String> {
	@Override
	public String convert(final StopAfter stopAfter) {
		if (stopAfter == null) return null;
		
		String convertedValue = null;
		final StopAfter.Type type = stopAfter.getType();
		switch (type) {
			case CURRENT_ITERATION:
				convertedValue = "current_iteration";
				break;
			case TIME:
				convertedValue = TimeDurationHelper.convertToString((Integer) stopAfter.getValue().get());
				break;
		}
		return convertedValue;
	}
}
