package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.validation.constraints.DurationRandomMaxGreaterThanMinCheck;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

public final class DurationRandomMaxGreaterThanMinValidator extends AbstractConstraintValidator<DurationRandomMaxGreaterThanMinCheck, DurationRandom> {
	private static final Pattern MILLISECONDS = Pattern.compile("\\d+");

	@Override
	public boolean isValid(final DurationRandom duration, final ConstraintValidatorContext context) {
		if (duration == null) {
			return true;
		}
		final Optional<Long> min = duration.getMin().map(DurationRandomMaxGreaterThanMinValidator::toMilliseconds).orElse(Optional.of(0L));
		final Optional<Long> max = toMilliseconds(duration.getMax());
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
