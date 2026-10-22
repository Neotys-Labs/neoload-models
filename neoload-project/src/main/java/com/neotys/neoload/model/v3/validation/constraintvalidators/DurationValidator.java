package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import javax.validation.ConstraintValidatorContext;

public final class DurationValidator extends AbstractConstraintValidator<CompositeCheck, LoadDuration> {
	@Override
	public boolean isValid(final LoadDuration duration, final ConstraintValidatorContext context) {
		if (duration == null || duration.getType() == null) {
			return false;
		}
		if (InvalidDuration.isInvalid(duration.getValue())) {
			return InvalidDuration.report(context, duration.getValue());
		}
		return duration.amount().orElse(0) > 0;
	}
}
