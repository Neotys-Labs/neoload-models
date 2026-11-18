package com.neotys.neoload.model.v3.binding.converter;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Reads a whole number of milliseconds written with its unit ({@code 40ms}) or without ({@code 40}).
 */
public final class StringToMillisecondsConverter extends StdConverter<String, Optional<Integer>> {
	static final String UNIT = "ms";
	private static final Pattern PATTERN = Pattern.compile("(-?[0-9]+)\\s*(" + UNIT + ")?");

	@Override
	public Optional<Integer> convert(final String input) {
		if (input == null || input.trim().isEmpty()) {
			return Optional.empty();
		}
		final Matcher matcher = PATTERN.matcher(input.trim());
		if (matcher.matches()) {
			try {
				return Optional.of(Integer.valueOf(matcher.group(1)));
			} catch (final NumberFormatException exception) {
				// too large: reported below
			}
		}
		throw new IllegalArgumentException("The value '" + input + "' is not a valid duration in milliseconds (for example 40ms).");
	}
}
