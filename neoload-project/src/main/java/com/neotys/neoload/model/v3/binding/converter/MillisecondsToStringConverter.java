package com.neotys.neoload.model.v3.binding.converter;

import java.util.Optional;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Writes a number of milliseconds with its unit, as {@code 40ms}.
 */
public final class MillisecondsToStringConverter extends StdConverter<Optional<Integer>, String> {
	@Override
	public String convert(final Optional<Integer> input) {
		return input.map(milliseconds -> milliseconds + StringToMillisecondsConverter.UNIT).orElse(null);
	}
}
