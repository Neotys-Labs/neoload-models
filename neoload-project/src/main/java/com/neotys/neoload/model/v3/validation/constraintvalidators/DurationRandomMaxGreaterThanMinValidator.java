package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.validation.constraints.DurationRandomMaxGreaterThanMinCheck;
import java.time.Duration;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;

public final class DurationRandomMaxGreaterThanMinValidator extends AbstractConstraintValidator<DurationRandomMaxGreaterThanMinCheck, DurationRandom> {
	@Override
	public boolean isValid(final DurationRandom duration, final ConstraintValidatorContext context) {
		if (duration == null) {
			return true;
		}
		final Optional<Duration> min = duration.getMin().map(DurationRandomMaxGreaterThanMinValidator::toDuration).orElse(Optional.of(Duration.ZERO));
		final Optional<Duration> max = toDuration(duration.getMax());
		if (!min.isPresent() || !max.isPresent()) {
			return true;
		}
		return max.get().compareTo(min.get()) > 0;
	}

	private static Optional<Duration> toDuration(final DurationOrVariable value) {
		if (value instanceof DurationLiteral) {
			return Optional.of(((DurationLiteral) value).getValue());
		}
		return Optional.empty();
	}
}
