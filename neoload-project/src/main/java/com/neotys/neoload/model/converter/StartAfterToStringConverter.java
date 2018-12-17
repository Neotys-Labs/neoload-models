package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.scenario.StartAfter;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public final class StartAfterToStringConverter extends StdConverter<StartAfter, String> {
	@Override
	public String convert(final StartAfter startAfter) {
		if (startAfter == null) return null;
		
		String convertedValue = null;
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
