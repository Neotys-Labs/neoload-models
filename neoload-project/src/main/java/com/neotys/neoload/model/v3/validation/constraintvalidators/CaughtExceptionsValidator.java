package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.validation.constraints.CaughtExceptionsCheck;
import javax.validation.ConstraintValidatorContext;

public final class CaughtExceptionsValidator extends AbstractConstraintValidator<CaughtExceptionsCheck, TryCatch> {
	@Override
	public boolean isValid(final TryCatch tryCatch, final ConstraintValidatorContext context) {
		return tryCatch.getCaughtExceptions().map(caughtExceptions -> !caughtExceptions.isEmpty()).orElse(true);
	}
}
