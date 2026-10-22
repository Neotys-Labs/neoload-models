package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import javax.validation.ConstraintValidatorContext;

public final class StopAfterValidator extends AbstractConstraintValidator<CompositeCheck, StopAfter> {
	@Override
	public boolean isValid(final StopAfter stopAfter, final ConstraintValidatorContext context) {
		if (stopAfter == null) {
			return false;
		}

		if (stopAfter.getInvalidText().isPresent()) {
			return true;
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
