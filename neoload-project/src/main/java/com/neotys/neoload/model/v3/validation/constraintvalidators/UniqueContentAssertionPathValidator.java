package com.neotys.neoload.model.v3.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionPathCheck;

public final class UniqueContentAssertionPathValidator extends AbstractConstraintValidator<UniqueContentAssertionPathCheck, ContentAssertion> {
	@Override
	public boolean isValid(final ContentAssertion assertion, final ConstraintValidatorContext context) {
		// null value is valid
		if (assertion == null) {
			return true;
		}
		return !(assertion.getJsonPath().isPresent() && assertion.getXPath().isPresent());
	}
}
