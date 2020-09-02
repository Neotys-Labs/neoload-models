package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;

public final class UniqueContentAssertionNameValidator extends AbstractConstraintValidator<UniqueContentAssertionNameCheck, List<ContentAssertion>> {
	@Override
	public boolean isValid(final List<ContentAssertion> assertions, final ConstraintValidatorContext context) {
		final Set<String> uniqueNames = new HashSet<>();
		int nullOrEmptyCount = 0;
		int total = 0;
		if ((assertions != null) && (!assertions.isEmpty())) {
			total = assertions.size();
			for (final ContentAssertion assertion : assertions) {
				final Optional<String> optionalName = assertion.getName();
				if (optionalName.isPresent()) {
					uniqueNames.add(optionalName.get());
				}
				else {
					nullOrEmptyCount = nullOrEmptyCount + 1;
				}
			}
		}
		return ((uniqueNames.size() + nullOrEmptyCount) == total);
	}
}
