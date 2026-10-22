package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;

public final class StartAfterToStringConverter extends StdConverter<StartAfter, String> {
	@Override
	public String convert(final StartAfter startAfter) {
		if (startAfter == null) return null;

		if (startAfter.getType() == StartAfter.Type.TIME) {
			return ((SecondsDuration) startAfter.getValue()).toText();
		}
		return (String) startAfter.getValue();
	}
}
