package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertion;
import com.neotys.neoload.model.v3.validation.constraints.UniqueDurationAssertionCheck;

public final class UniqueDurationAssertionValidator extends AbstractConstraintValidator<UniqueDurationAssertionCheck, List<Assertion>> {

	@Override
	public boolean isValid(final List<Assertion> assertions, final ConstraintValidatorContext context) {
		if (assertions == null || assertions.isEmpty()) {
			return true;
		}
		int count = 0;
		for (final Assertion assertion : assertions) {
			if (assertion instanceof DurationAssertion) {
				count++;
				if (count > 1) {
					return false;
				}
			}
		}
		return true;
	}
}
