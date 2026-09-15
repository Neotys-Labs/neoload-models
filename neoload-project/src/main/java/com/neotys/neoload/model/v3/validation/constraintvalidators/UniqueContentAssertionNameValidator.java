package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.ConstraintValidatorContext;

public final class UniqueContentAssertionNameValidator extends AbstractConstraintValidator<UniqueContentAssertionNameCheck, List<ContentAssertion>> {
	@Override
	public boolean isValid(final List<ContentAssertion> assertions, final ConstraintValidatorContext context) {
		if ((assertions != null) && (!assertions.isEmpty())) {
			return doValid(assertions);
		}
		return true;
	}

	private static boolean doValid(final List<ContentAssertion> assertions) {
		final Set<String> uniqueNames = new HashSet<>();
		final AtomicInteger nullOrEmptyCount = new AtomicInteger(0);
		final int total = assertions.size();

		for (final ContentAssertion assertion : assertions) {
			doValid(assertion, uniqueNames, nullOrEmptyCount);
		}

		return ((uniqueNames.size() + nullOrEmptyCount.get()) == total);
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
