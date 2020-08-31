package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.WhenRelease;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;

import javax.validation.ConstraintValidatorContext;

public final class WhenReleaseValidator extends AbstractConstraintValidator<CompositeCheck, WhenRelease> {
	@Override
	public boolean isValid(final WhenRelease whenRelease, final ConstraintValidatorContext context) {
		if (whenRelease == null) {
			return false;
		}

		final WhenRelease.Type type = whenRelease.getType();
		if (type == null) {
			return false;
		}

		final Object value = whenRelease.getValue();
		switch (type){
			case PERCENTAGE:
			case VU_NUMBER:
				if (value == null) {
					return false;
				}
				if (!(value instanceof Integer)) {
					return false;
				}
				return ((Integer) value > 0);
			case MANUAL:
				return "manual".equalsIgnoreCase((String) value);
		}
		return true;

	}
}
