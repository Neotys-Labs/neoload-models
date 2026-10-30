package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.MaxSecondsCheck;
import java.time.Duration;
import javax.validation.ConstraintValidatorContext;

public final class MaxSecondsValidator extends AbstractConstraintValidator<MaxSecondsCheck, Duration> {
	private static final Duration MAX = Duration.ofSeconds(MaxSecondsCheck.MAX_SECONDS);

	@Override
	public boolean isValid(final Duration value, final ConstraintValidatorContext context) {
		return value == null || value.compareTo(MAX) <= 0;
	}
}
