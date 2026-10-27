package com.neotys.neoload.model.v3.binding.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IterationDurationHelper {
	private static final Pattern ITERATION_PATTERN = Pattern.compile("(\\d+) ?iterations?");

	private IterationDurationHelper() {
		super();
	}

	/**
	 * @return the number of iterations, or null if the text is not an iteration count that fits in an {@code int}
	 */
	public static Integer convertToInteger(final String input) {
		final Matcher matcher = ITERATION_PATTERN.matcher(input);
		if (!matcher.matches()) {
			return null;
		}
		try {
			return Integer.valueOf(matcher.group(1));
		} catch (final NumberFormatException e) {
			return null;
		}
	}
}
