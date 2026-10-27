package com.neotys.neoload.model.v3.project.userpath;

import java.time.Duration;

public final class Durations {
	private Durations() {
	}

	public static DurationOrVariable of(final String millisecondsOrVariable) {
		if (millisecondsOrVariable.startsWith("${")) {
			return DurationVariable.builder().value(millisecondsOrVariable).build();
		}
		return DurationLiteral.builder().value(Duration.ofMillis(Long.parseLong(millisecondsOrVariable))).build();
	}
}
