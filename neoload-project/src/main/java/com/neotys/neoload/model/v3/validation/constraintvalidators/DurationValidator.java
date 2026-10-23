package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import javax.validation.ConstraintValidatorContext;

public final class DurationValidator extends AbstractConstraintValidator<CompositeCheck, LoadDuration> {
	@Override
	public boolean isValid(final LoadDuration duration, final ConstraintValidatorContext context) {
		
		if (duration == null) return false;
		if (duration.getInvalidText().isPresent()) return true;
		
		final LoadDuration.Type type = duration.getType();
		if (type == null) return false;

		final Integer value = duration.getValue();
		if (value == null) return false;
		return value > 0;
	}
}
