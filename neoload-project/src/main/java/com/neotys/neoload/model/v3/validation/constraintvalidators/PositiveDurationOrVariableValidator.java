package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import javax.validation.ConstraintValidatorContext;

public final class PositiveDurationOrVariableValidator extends AbstractConstraintValidator<PositiveCheck, DurationOrVariable> {
	@Override
	public boolean isValid(final DurationOrVariable value, final ConstraintValidatorContext context) {
		if (!(value instanceof DurationLiteral)) {
			return true;
		}
		final java.time.Duration duration = ((DurationLiteral) value).getValue();
		return duration == null || (!duration.isZero() && !duration.isNegative());
	}
}
