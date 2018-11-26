package com.neotys.neoload.model.v3.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.scenario.Duration;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;

public final class DurationValidator extends AbstractConstraintValidator<CompositeCheck, Duration> {
	@Override
	public boolean isValid(final Duration duration, final ConstraintValidatorContext context) {
		
		if (duration == null) return false;
		
		final Duration.Type type = duration.getType();
		if (type == null) return false;

		final Integer value = duration.getValue();
		if (value == null) return false;
		return value > 0;
	}
}
