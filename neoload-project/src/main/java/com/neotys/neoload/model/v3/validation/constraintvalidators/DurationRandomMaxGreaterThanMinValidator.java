package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.binding.converter.DurationGrammar;
import com.neotys.neoload.model.v3.binding.converter.Durations;
import com.neotys.neoload.model.v3.binding.converter.InvalidDurationException;
import com.neotys.neoload.model.v3.binding.converter.ParsedDuration;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.validation.constraints.DurationRandomMaxGreaterThanMinCheck;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;

public final class DurationRandomMaxGreaterThanMinValidator extends AbstractConstraintValidator<DurationRandomMaxGreaterThanMinCheck, DurationRandom> {
	@Override
	public boolean isValid(final DurationRandom duration, final ConstraintValidatorContext context) {
		if (duration == null) {
			return true;
		}
		final Optional<Long> min = duration.getMinInMsOrVariable().map(DurationRandomMaxGreaterThanMinValidator::toMilliseconds).orElse(Optional.of(0L));
		final Optional<Long> max = toMilliseconds(duration.getMaxInMsOrVariable());
		if (!min.isPresent() || !max.isPresent()) {
			return true;
		}
		return max.get() > min.get();
	}

	private static Optional<Long> toMilliseconds(final String value) {
		try {
			final ParsedDuration parsed = Durations.parse(value, DurationGrammar.MILLISECONDS);
			return parsed.isVariable() ? Optional.empty() : Optional.of(parsed.getAmount());
		} catch (final InvalidDurationException e) {
			return Optional.empty();
		}
	}
}
