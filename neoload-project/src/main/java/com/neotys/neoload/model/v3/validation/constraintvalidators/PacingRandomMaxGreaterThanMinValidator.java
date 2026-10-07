package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.PacingRandom;
import com.neotys.neoload.model.v3.validation.constraints.PacingRandomMaxGreaterThanMinCheck;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

public final class PacingRandomMaxGreaterThanMinValidator extends AbstractConstraintValidator<PacingRandomMaxGreaterThanMinCheck, PacingRandom> {
	private static final Pattern UNIT_SEGMENT = Pattern.compile("(\\d+)(h|ms|m|s)");
	private static final Pattern PLAIN_NUMBER = Pattern.compile("\\d+");

	@Override
	public boolean isValid(final PacingRandom pacingRandom, final ConstraintValidatorContext context) {
		if (pacingRandom == null) {
			return true;
		}

		final Optional<Long> min = toMilliseconds(pacingRandom.getMin());
		final Optional<Long> max = toMilliseconds(pacingRandom.getMax());
		if (!min.isPresent() || !max.isPresent()) {
			// unparsable or variable-backed values are left to the pattern check
			return true;
		}
		return max.get() > min.get();
	}

	private static Optional<Long> toMilliseconds(final String value) {
		if (value == null || value.startsWith("${")) {
			return Optional.empty();
		}
		if (PLAIN_NUMBER.matcher(value).matches()) {
			return Optional.of(Long.parseLong(value) * 1_000L);
		}

		final Matcher matcher = UNIT_SEGMENT.matcher(value);
		long totalMs = 0;
		int lastEnd = 0;
		boolean matchedAny = false;
		while (matcher.find()) {
			if (matcher.start() != lastEnd) {
				return Optional.empty();
			}
			matchedAny = true;
			final long amount = Long.parseLong(matcher.group(1));
			switch (matcher.group(2)) {
				case "h": totalMs += amount * 3_600_000L; break;
				case "m": totalMs += amount * 60_000L; break;
				case "s": totalMs += amount * 1_000L; break;
				case "ms": totalMs += amount; break;
				default: return Optional.empty();
			}
			lastEnd = matcher.end();
		}
		if (!matchedAny || lastEnd != value.length()) {
			return Optional.empty();
		}
		return Optional.of(totalMs);
	}
}
