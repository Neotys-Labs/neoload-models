package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.validation.constraints.NonZeroCheck;
import javax.validation.ConstraintValidatorContext;

/**
 * A constant duration must not be a literal 0; a random duration or a variable reference is left alone.
 */
public final class NonZeroDurationValidator extends AbstractConstraintValidator<NonZeroCheck, Duration> {
	@Override
	public boolean isValid(final Duration value, final ConstraintValidatorContext context) {
		if (!(value instanceof DurationConstant) || !(((DurationConstant) value).getValue() instanceof DurationLiteral)) {
			return true;
		}
		final java.time.Duration duration = ((DurationLiteral) ((DurationConstant) value).getValue()).getValue();
		return duration == null || !duration.isZero();
	}
}
