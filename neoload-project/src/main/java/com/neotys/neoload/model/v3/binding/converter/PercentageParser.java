package com.neotys.neoload.model.v3.binding.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The single text syntax of an integer percentage: an integer with an optional {@code %}, for instance
 * {@code 150%} or {@code 150}, without surrounding spaces. A percentage is always written with the {@code %}.
 * The sign is read, whether a field accepts a negative or a zero value is checked by validation, not here.
 */
public final class PercentageParser {
	private static final Pattern PERCENTAGE = Pattern.compile("(-?\\d+)%?");

	private PercentageParser() {
	}

	/**
	 * @throws InvalidPercentageException if the text is not an integer percentage, or does not fit in an {@code int}
	 */
	public static int parse(final String text) {
		if (text == null) {
			throw new InvalidPercentageException(null);
		}
		final Matcher matcher = PERCENTAGE.matcher(text);
		if (!matcher.matches()) {
			throw new InvalidPercentageException(text);
		}
		try {
			return Integer.parseInt(matcher.group(1));
		} catch (final NumberFormatException e) {
			throw new InvalidPercentageException(text);
		}
	}

	public static String format(final int percentage) {
		return percentage + "%";
	}
}
