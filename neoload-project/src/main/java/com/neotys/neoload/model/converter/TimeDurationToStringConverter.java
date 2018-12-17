package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public final class TimeDurationToStringConverter extends StdConverter<Integer, String> {
	@Override
	public String convert(final Integer input) {
		if (input != null) {
			return TimeDurationHelper.convertToString(input);
		}
		return null;
	}
}
