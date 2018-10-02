package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.scenario.StartAfter;

public final class StartAfterToStringConverter extends StdConverter<StartAfter, String> {
	@Override
	public String convert(final StartAfter startAfter) {
		if (startAfter == null) return null;
		
		//final Duration duration = optional.get();
		final StartAfter.Type type = startAfter.getType();
		switch (type) {
			case POPULATION:
				return (String) startAfter.getValue();
			case TIME:
				return TimeDurationHelper.convertToString((Integer) startAfter.getValue());
			default:
				return null;
		}
	}
}
