package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.TimeLoadDuration;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import java.time.Duration;
import javax.validation.ConstraintValidatorContext;

public final class DurationValidator extends AbstractConstraintValidator<CompositeCheck, LoadDuration> {
	@Override
	public boolean isValid(final LoadDuration duration, final ConstraintValidatorContext context) {
		if (duration == null) return false;
		if (duration instanceof TimeLoadDuration) {
			final Duration value = ((TimeLoadDuration) duration).getValue();
			return value != null && !value.isNegative() && !value.isZero();
		}
		return duration.getAmount() > 0;
	}
}
