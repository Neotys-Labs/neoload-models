package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.scenario.StartAfter;

public final class StartAfterToStringConverter extends StdConverter<StartAfter, String> {
	@Override
	public String convert(final StartAfter startAfter) {
		if (startAfter == null) return null;
		
		String convertedValue = null;
		final StartAfter.Type type = startAfter.getType();
		switch (type) {
			case POPULATION:
				convertedValue = (String) startAfter.getValue();
				break;
			case TIME:
				convertedValue = TimeDurationHelper.convertToString((Integer) startAfter.getValue());
				break;
		}
		return convertedValue;
	}
}
