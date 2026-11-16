package com.neotys.neoload.model.readers.jmeter.step.timer;

import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationVariable;
import java.time.Duration;
import java.util.regex.Pattern;

/**
 * Converts the delay of a JMeter timer, a number of milliseconds or a variable reference.
 */
public final class TimerDuration {
	private static final Pattern MILLISECONDS = Pattern.compile("\\d+");

	private TimerDuration() {
	}

	public static DurationOrVariable of(final String delay) {
		if (MILLISECONDS.matcher(delay).matches()) {
			return DurationLiteral.builder().value(Duration.ofMillis(Long.parseLong(delay))).build();
		}
		return DurationVariable.builder().value(delay).build();
	}
}
