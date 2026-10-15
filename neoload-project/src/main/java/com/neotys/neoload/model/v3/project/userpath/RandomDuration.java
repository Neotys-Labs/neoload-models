package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.project.MinMaxRange;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * A duration drawn between a {@code min} and a {@code max}, compared in milliseconds. A bound given as
 * a plain number is expressed in {@link #plainNumberUnit()}.
 */
interface RandomDuration extends MinMaxRange {
	String getMin();

	String getMax();

	default TimeUnit plainNumberUnit() {
		return TimeUnit.MILLISECONDS;
	}

	@Override
	default Optional<BigDecimal> comparableMin() {
		return Durations.toMilliseconds(getMin(), plainNumberUnit());
	}

	@Override
	default Optional<BigDecimal> comparableMax() {
		return Durations.toMilliseconds(getMax(), plainNumberUnit());
	}
}
