package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;

public final class UniqueContentAssertionNameValidator extends AbstractConstraintValidator<UniqueContentAssertionNameCheck, List<Assertion>> {
	@Override
	public boolean isValid(final List<Assertion> assertions, final ConstraintValidatorContext context) {
		if ((assertions != null) && (!assertions.isEmpty())) {
			return doValid(assertions);
		}
		return true;
	}
	
	private static boolean doValid(final List<Assertion> assertions) {
		// Content Assertion
		final Set<String> contentAssertionUniqueNames = new HashSet<>();
		final AtomicInteger contentAssertionNullOrEmptyCount = new AtomicInteger(0);
		// Other Assertion
		int otherAssertionCount = 0;
		// Total
		int total = assertions.size();
		
		// Count the number of unique names for Content Assertion
		for (final Assertion assertion : assertions) {
			if (ContentAssertion.class.isInstance(assertion)) {
				doValid((ContentAssertion) assertion, contentAssertionUniqueNames, contentAssertionNullOrEmptyCount);
			}
			else {
				otherAssertionCount = otherAssertionCount + 1;
			}
		}
		
		return ((contentAssertionUniqueNames.size() + contentAssertionNullOrEmptyCount.get() + otherAssertionCount) == total);
	}
	
	private static void doValid(final ContentAssertion assertion, final Set<String> uniqueNames, final AtomicInteger nullOrEmptyCount) {
		final Optional<String> optionalName = assertion.getName();
		if (optionalName.isPresent()) {
			uniqueNames.add(optionalName.get());
		}
		else {
			nullOrEmptyCount.incrementAndGet();
		}
	}	
}
