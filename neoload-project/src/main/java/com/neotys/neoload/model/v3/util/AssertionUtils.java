package com.neotys.neoload.model.v3.util;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

public class AssertionUtils {

	protected static final String PREFIX_NAME = "assertion_";
	
	private static final String EMPTY = "";

	private AssertionUtils() {
	}

	public static List<Assertion> normalyze(final List<Assertion> assertions) {
		// Retrieves all names
		final Set<String> names = getNames(assertions);
		
		// Constructs a new list of assertions with the filled name
		final AtomicInteger index = new AtomicInteger(1);
		final ImmutableList.Builder<Assertion> copiedAssertions = new ImmutableList.Builder<>();
		for (final Assertion assertion : assertions) {
			if (ContentAssertion.class.isInstance(assertion)) {
				copiedAssertions.add(normalyze((ContentAssertion) assertion, names, index));				
			}
			else {
				copiedAssertions.add(assertion);
			}
		}
		return copiedAssertions.build();
	}
	
	public static String normalyzeContains(final Optional<String> contains) {
		return contains.orElse(EMPTY);
	}

	private static Assertion normalyze(final ContentAssertion assertion, final Set<String> names, final AtomicInteger index) {
		final Optional<String> name = assertion.getName();
		if (name.isPresent()) {
			// If name exists, use the given assertion
			return assertion;
		}
		else {				
			// If name doesn't exist, compute a name and do a copy from the given assertion with the computed name
			String computedName = "";
			do {
				computedName = computeName(index.getAndIncrement());				
			}
			while (contains(names, computedName));
			names.add(computedName);
			
			return ContentAssertion.builder()
					.from(assertion)
					.name(computedName)
					.build();
		}
	}

	private static Set<String> getNames(final List<Assertion> assertions) {
		final Set<String> names = Sets.newHashSet();
		assertions.stream()
			.filter(assertion -> ContentAssertion.class.isInstance(assertion))
			.forEach(assertion -> ((ContentAssertion) assertion).getName().ifPresent(names::add));
		return names;
	}
	
	private static String computeName(final int index) {
		return new StringBuilder()
				.append(PREFIX_NAME)
				.append(index)
				.toString();
	}
	
	private static boolean contains(final Set<String> names, final String name) {
		return names.contains(name);
	}
}
