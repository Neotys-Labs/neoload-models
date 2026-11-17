package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.validation.constraints.PositiveOrZeroCheck;
import javax.validation.ConstraintValidatorContext;

/**
 * A literal duration must not be negative; a variable reference is left alone.
 */
public final class PositiveOrZeroDurationOrVariableValidator extends AbstractConstraintValidator<PositiveOrZeroCheck, DurationOrVariable> {
	@Override
	public boolean isValid(final DurationOrVariable value, final ConstraintValidatorContext context) {
		if (!(value instanceof DurationLiteral)) {
			return true;
		}
		final java.time.Duration duration = ((DurationLiteral) value).getValue();
		return duration == null || !duration.isNegative();
	}
}
