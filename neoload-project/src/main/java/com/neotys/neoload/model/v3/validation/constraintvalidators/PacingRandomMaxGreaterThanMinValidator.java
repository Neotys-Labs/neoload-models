package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.validation.constraints.PacingRandomMaxGreaterThanMinCheck;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

public final class PacingRandomMaxGreaterThanMinValidator extends AbstractConstraintValidator<PacingRandomMaxGreaterThanMinCheck, Duration> {
	private static final Pattern MILLISECONDS = Pattern.compile("\\d+");

	@Override
	public boolean isValid(final Duration duration, final ConstraintValidatorContext context) {
		if (!(duration instanceof DurationRandom)) {
			return true;
		}
		final DurationRandom random = (DurationRandom) duration;
		final Optional<Long> min = random.getMin().map(PacingRandomMaxGreaterThanMinValidator::toMilliseconds).orElse(Optional.of(0L));
		final Optional<Long> max = toMilliseconds(random.getMax());
		if (!min.isPresent() || !max.isPresent()) {
			return true;
		}
		return max.get() > min.get();
	}

	private static Optional<Long> toMilliseconds(final String value) {
		if (value == null || !MILLISECONDS.matcher(value).matches()) {
			return Optional.empty();
		}
		return Optional.of(Long.parseLong(value));
	}
}
