package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.validation.constraints.SecondsDurationCheck;
import javax.validation.ConstraintValidatorContext;

public final class SecondsDurationValidator extends AbstractConstraintValidator<SecondsDurationCheck, SecondsDuration> {
	@Override
	public boolean isValid(final SecondsDuration duration, final ConstraintValidatorContext context) {
		return duration == null || !duration.isInvalid();
	}
}
