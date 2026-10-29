package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import java.time.Duration;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;

public final class DurationRandomMinNotGreaterThanMaxValidator extends AbstractConstraintValidator<MinNotGreaterThanMaxCheck, DurationRandom> {
	@Override
	public boolean isValid(final DurationRandom duration, final ConstraintValidatorContext context) {
		if (duration == null) {
			return true;
		}
		final Optional<Duration> min = duration.getMin().map(DurationRandomMinNotGreaterThanMaxValidator::toDuration).orElse(Optional.of(Duration.ZERO));
		final Optional<Duration> max = toDuration(duration.getMax());
		if (!min.isPresent() || !max.isPresent()) {
			return true;
		}
		return min.get().compareTo(max.get()) <= 0;
	}

	private static Optional<Duration> toDuration(final DurationOrVariable value) {
		if (value instanceof DurationLiteral) {
			return Optional.ofNullable(((DurationLiteral) value).getValue());
		}
		return Optional.empty();
	}
}
