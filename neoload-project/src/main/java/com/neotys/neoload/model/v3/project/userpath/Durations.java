package com.neotys.neoload.model.v3.project.userpath;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts an as-code duration (a plain number, or {@code h}/{@code m}/{@code s}/{@code ms} suffixed
 * components such as {@code 1m30s}) into milliseconds, so that the bounds of a random duration can be
 * compared. A variable or a malformed duration has no value.
 */
final class Durations {
	private static final Pattern PLAIN_NUMBER = Pattern.compile("\\d+(\\.\\d+)?");
	private static final Pattern UNIT_SEGMENT = Pattern.compile("(\\d+)(h|ms|m|s)");

	private Durations() {
	}

	static Optional<BigDecimal> toMilliseconds(final String duration, final TimeUnit plainNumberUnit) {
		if (duration == null) {
			return Optional.empty();
		}
		if (PLAIN_NUMBER.matcher(duration).matches()) {
			return Optional.of(inMilliseconds(new BigDecimal(duration), plainNumberUnit));
		}
		return unitSegmentsToMilliseconds(duration);
	}

	private static Optional<BigDecimal> unitSegmentsToMilliseconds(final String duration) {
		final Matcher matcher = UNIT_SEGMENT.matcher(duration);
		BigDecimal total = BigDecimal.ZERO;
		int consumed = 0;
		while (matcher.find() && matcher.start() == consumed) {
			total = total.add(inMilliseconds(new BigDecimal(matcher.group(1)), toTimeUnit(matcher.group(2))));
			consumed = matcher.end();
		}
		if (consumed == 0 || consumed != duration.length()) {
			return Optional.empty();
		}
		return Optional.of(total);
	}

	private static TimeUnit toTimeUnit(final String unit) {
		switch (unit) {
			case "h":
				return TimeUnit.HOURS;
			case "m":
				return TimeUnit.MINUTES;
			case "s":
				return TimeUnit.SECONDS;
			default:
				return TimeUnit.MILLISECONDS;
		}
	}

	private static BigDecimal inMilliseconds(final BigDecimal amount, final TimeUnit unit) {
		return amount.multiply(BigDecimal.valueOf(unit.toMillis(1)));
	}
}
