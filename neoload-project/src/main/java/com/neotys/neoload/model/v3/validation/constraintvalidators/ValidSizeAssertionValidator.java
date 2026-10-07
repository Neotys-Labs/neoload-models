package com.neotys.neoload.model.v3.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;
import com.neotys.neoload.model.v3.validation.constraints.ValidSizeAssertionCheck;

public final class ValidSizeAssertionValidator extends AbstractConstraintValidator<ValidSizeAssertionCheck, SizeAssertion> {

	@Override
	public boolean isValid(final SizeAssertion assertion, final ConstraintValidatorContext context) {
		if (assertion == null) {
			return true;
		}

		final boolean hasEquals = assertion.getEquals().isPresent();
		final boolean hasBound = assertion.getGreaterThan().isPresent() || assertion.getLessThan().isPresent();

		if (hasEquals && hasBound) {
			return false;
		}

		return hasEquals || hasBound;
	}
}
