package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import com.neotys.neoload.model.v3.validation.constraints.WholeSecondsCheck;
import javax.validation.ConstraintValidatorContext;

public final class TimeLoadDurationWholeSecondsValidator extends AbstractConstraintValidator<WholeSecondsCheck, TimeLoadDuration> {
	@Override
	public boolean isValid(final TimeLoadDuration duration, final ConstraintValidatorContext context) {
		return duration == null || duration.getValue() == null || duration.getValue().getNano() == 0;
	}
}
