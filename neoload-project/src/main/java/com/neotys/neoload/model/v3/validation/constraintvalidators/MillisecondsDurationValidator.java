package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;
import com.neotys.neoload.model.v3.validation.constraints.MillisecondsDurationCheck;
import javax.validation.ConstraintValidatorContext;

public final class MillisecondsDurationValidator extends AbstractConstraintValidator<MillisecondsDurationCheck, MillisecondsDuration> {
	@Override
	public boolean isValid(final MillisecondsDuration duration, final ConstraintValidatorContext context) {
		return duration == null || !duration.isInvalid();
	}
}
