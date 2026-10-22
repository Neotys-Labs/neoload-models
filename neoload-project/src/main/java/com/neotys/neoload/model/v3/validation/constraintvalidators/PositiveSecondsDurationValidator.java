package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import javax.validation.ConstraintValidatorContext;

/**
 * An invalid duration is reported by the {@code SecondsDurationCheck} on the same field, not here.
 */
public final class PositiveSecondsDurationValidator extends AbstractConstraintValidator<PositiveCheck, SecondsDuration> {
	@Override
	public boolean isValid(final SecondsDuration duration, final ConstraintValidatorContext context) {
		return duration == null || duration.isInvalid() || duration.getAmount() > 0;
	}
}
