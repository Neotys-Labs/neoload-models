package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import java.time.Duration;
import javax.validation.ConstraintValidatorContext;

public final class PositiveDurationValidator extends AbstractConstraintValidator<PositiveCheck, Duration> {
	@Override
	public boolean isValid(final Duration value, final ConstraintValidatorContext context) {
		return value == null || (!value.isZero() && !value.isNegative());
	}
}
