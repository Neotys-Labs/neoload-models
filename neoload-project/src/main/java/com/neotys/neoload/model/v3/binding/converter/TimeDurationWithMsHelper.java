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

	private static final Pattern TIME_PATTERN = Pattern.compile("((((\\d+)(h\\s?))?((\\d+)(m\\s?))?((\\d+)(s\\s?))?((\\d+)(ms\\s?))?)|(\\d+))");

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

	/**
	 * Convert a string to millisecond
	 * 2h 3m 4s 5ms 	-> 7384005
	 * 3m 100ms 		-> 18100
	 * 1000				-> 1000
	 * @param input
	 * @return
	 */
	protected static String convertToDuration(final String input) {
		if (isNullOrEmpty(input)) {
			return "0";
		}

		final Matcher matcher = TIME_PATTERN.matcher(input.trim());
		if (!matcher.matches()) {
			return "0";
		}

		if ((matcher.groupCount() == 15 && matcher.group(15) != null)) {
			return input;
		}

		int hourNumber = extractNumberFromGroup(matcher.group(4));
		final int minuteNumber = extractNumberFromGroup(matcher.group(7));
		final int secondNumber = extractNumberFromGroup(matcher.group(10));
		final int milliSecondNumber = extractNumberFromGroup(matcher.group(13));

		return String.valueOf(
				Duration.ofHours(hourNumber).toMillis()
				+ Duration.ofMinutes(minuteNumber).toMillis()
				+ Duration.ofSeconds(secondNumber).toMillis()
				+ Duration.ofMillis(milliSecondNumber).toMillis());
	}

	private static int extractNumberFromGroup(final String group) {
		if (group != null) {
			return Integer.valueOf(group);
		}
		return 0;
	}
}
