package com.neotys.neoload.model.v3.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;

public final class StopAfterValidator extends AbstractConstraintValidator<CompositeCheck, StopAfter> {
	@Override
	public boolean isValid(final StopAfter stopAfter, final ConstraintValidatorContext context) {
		if (stopAfter == null) {
			return false;
		}

		final StopAfter.Type type = stopAfter.getType();
		if (type == null) {
			return false;
		}

		final Object value = stopAfter.getValue();		
		if (type == StopAfter.Type.TIME) {
			if (value == null) {
				return false;
			}
			if (!(value instanceof Integer)) {
				return false;
			}
			return ((Integer) value > 0);
		}
		else {
			return (value == null);
		}		
	}
}
