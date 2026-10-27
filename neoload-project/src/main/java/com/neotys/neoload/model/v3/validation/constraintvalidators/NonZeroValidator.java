package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.NonZeroCheck;
import javax.validation.ConstraintValidatorContext;

public final class NonZeroValidator extends AbstractConstraintValidator<NonZeroCheck, Number> {
	@Override
	public boolean isValid(final Number value, final ConstraintValidatorContext context) {
		return value == null || value.doubleValue() != 0;
	}
}
