package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;

public final class StartAfterToStringConverter extends StdConverter<StartAfter, String> {
	@Override
	public String convert(final StartAfter startAfter) {
		if (startAfter == null) return null;
		
		String convertedValue;
		final StartAfter.Type type = startAfter.getType();
		if (type == StartAfter.Type.TIME) {
			convertedValue = TimeDurationHelper.convertToString((Integer) startAfter.getValue());
		}
		else {
			convertedValue = (String) startAfter.getValue();
		}
		return convertedValue;
	}
}
