package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import javax.validation.ConstraintValidatorContext;

public final class PositiveNumberValidator extends AbstractConstraintValidator<PositiveCheck, Number> {
	@Override
	public boolean isValid(final Number value, final ConstraintValidatorContext context) {
		return value == null || value.doubleValue() > 0;
	}
}
