package com.neotys.neoload.model.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.base.Strings;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public final class StringToTimeDurationConverter extends StdConverter<String, Integer> {
	private static final Integer ERROR_VALUE = Integer.MIN_VALUE;

	@Override
	public Integer convert(final String input) {
		if (Strings.isNullOrEmpty(input)) {
			return ERROR_VALUE;
		}
		
		final Integer value = TimeDurationHelper.convertToInteger(input);
		if (value != null) {
			return value;
		}
		return ERROR_VALUE;
	}
}
