package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.WholeSecondsCheck;
import java.time.Duration;
import javax.validation.ConstraintValidatorContext;

public final class WholeSecondsValidator extends AbstractConstraintValidator<WholeSecondsCheck, Duration> {
	@Override
	public boolean isValid(final Duration value, final ConstraintValidatorContext context) {
		return value == null || value.getNano() == 0;
	}
}
