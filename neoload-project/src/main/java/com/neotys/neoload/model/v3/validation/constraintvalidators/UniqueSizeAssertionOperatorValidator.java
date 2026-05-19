package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeOperator;
import com.neotys.neoload.model.v3.validation.constraints.UniqueSizeAssertionOperatorCheck;

public final class UniqueSizeAssertionOperatorValidator extends AbstractConstraintValidator<UniqueSizeAssertionOperatorCheck, List<Assertion>> {

	@Override
	public boolean isValid(final List<Assertion> assertions, final ConstraintValidatorContext context) {
		if (assertions == null || assertions.isEmpty()) {
			return true;
		}
		final Set<SizeOperator> seen = EnumSet.noneOf(SizeOperator.class);
		for (final Assertion assertion : assertions) {
			if (assertion instanceof SizeAssertion) {
				final SizeOperator operator = ((SizeAssertion) assertion).getOperator();
				if (operator != null && !seen.add(operator)) {
					return false;
				}
			}
		}
		return true;
	}
}
