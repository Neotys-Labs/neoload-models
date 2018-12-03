package com.neotys.neoload.model.v3.binding.converter;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;

final class TimeDurationWithMsHelper {
	private static final String ZERO = "0";

	private static final String HOURS = "h";
	private static final String MINUTES = "m";
	private static final String SECONDS = "s";
	private static final String MILLISECONDS = "ms";

	private static final Pattern TIME_PATTERN = Pattern.compile("((\\d+)(h))?((\\d+)(m))?((\\d+)(s))?((\\d+)(ms))?");

	private TimeDurationWithMsHelper() {
		super();
	}

	protected static String convertToString(final Integer inputInMilliSecond) {
		if (inputInMilliSecond == null) return null;
		if (inputInMilliSecond <= 0) return ZERO;

		Duration duration = Duration.ofMillis(inputInMilliSecond);
		final long hours = duration.toHours();
		duration = duration.minusHours(hours);
		final long minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);
		final long seconds = duration.getSeconds();
		duration = duration.minusSeconds(seconds);
		final long milliSeconds = duration.toMillis();

		final StringBuilder sb = new StringBuilder();
		if (hours != 0) {
			sb.append(hours).append(HOURS);
		}
		if (minutes != 0) {
			sb.append(minutes).append(MINUTES);
		}
		if (seconds != 0) {
			sb.append(seconds).append(SECONDS);
		}
		if (milliSeconds != 0) {
			sb.append(milliSeconds).append(MILLISECONDS);
		}
		return sb.toString();
	}

	protected static Long convertToLong(final String input) {
		if (isNullOrEmpty(input)) {
			return 0L;
		}
		final String inputWithoutWhitespace = input.replaceAll("\\s+", "");
		final Matcher matcher = TIME_PATTERN.matcher(inputWithoutWhitespace);
		if (!matcher.matches() || (matcher.groupCount() != 12)) {
			return 0L;
		}

		int hourNumber = extractNumberFromGroup(matcher.group(2));
		final int minuteNumber = extractNumberFromGroup(matcher.group(5));
		final int secondNumber = extractNumberFromGroup(matcher.group(8));
		final int milliSecondNumber = extractNumberFromGroup(matcher.group(11));

		return Duration.ofHours(hourNumber).toMillis()
				+ Duration.ofMinutes(minuteNumber).toMillis()
				+ Duration.ofSeconds(secondNumber).toMillis()
				+ Duration.ofMillis(milliSecondNumber).toMillis();
	}

	private static int extractNumberFromGroup(final String group) {
		if (group != null) {
			return Integer.valueOf(group);
		}
		return 0;
	}
}
