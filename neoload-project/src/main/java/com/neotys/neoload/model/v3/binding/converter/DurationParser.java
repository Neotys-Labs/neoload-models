package com.neotys.neoload.model.v3.binding.converter;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The single text syntax of a duration: ordered units {@code h m s ms} (for instance {@code 1h30m15s500ms}) or a
 * plain number, whose unit is the one of the field. Durations are written compact ({@code 1m30s}), zero being
 * {@code 0}. Whether a field accepts a milliseconds part is checked by validation, not here.
 */
public final class DurationParser {
	private static final Pattern PLAIN_NUMBER = Pattern.compile("\\d+");
	private static final Pattern UNITS = Pattern.compile("(?=\\d)(?:(\\d+)h\\s*)?(?:(\\d+)m\\s*)?(?:(\\d+)s\\s*)?(?:(\\d+)ms)?");

	private DurationParser() {
	}

	/**
	 * @throws InvalidDurationException if the text is not a valid duration, or does not fit in a {@link Duration}
	 */
	public static Duration parse(final String text, final TemporalUnit plainNumberUnit) {
		if (text == null) {
			throw new InvalidDurationException(null);
		}
		final String trimmed = text.trim();
		try {
			if (PLAIN_NUMBER.matcher(trimmed).matches()) {
				return Duration.of(Long.parseLong(trimmed), plainNumberUnit);
			}
			final Matcher matcher = UNITS.matcher(trimmed);
			if (!matcher.matches()) {
				throw new InvalidDurationException(text);
			}
			return Duration.ofHours(amount(matcher, 1))
					.plusMinutes(amount(matcher, 2))
					.plusSeconds(amount(matcher, 3))
					.plusMillis(amount(matcher, 4));
		} catch (final NumberFormatException | ArithmeticException e) {
			throw new InvalidDurationException(text);
		}
	}

	private static long amount(final Matcher matcher, final int group) {
		final String digits = matcher.group(group);
		return digits == null ? 0 : Long.parseLong(digits);
	}

	public static String format(final Duration duration) {
		if (duration.isZero() || duration.isNegative()) {
			return "0";
		}
		final StringBuilder text = new StringBuilder();
		append(text, duration.toHours(), "h");
		append(text, duration.toMinutesPart(), "m");
		append(text, duration.toSecondsPart(), "s");
		append(text, duration.toMillisPart(), "ms");
		return text.length() == 0 ? "0" : text.toString();
	}

	private static void append(final StringBuilder text, final long amount, final String unit) {
		if (amount != 0) {
			text.append(amount).append(unit);
		}
	}
}
