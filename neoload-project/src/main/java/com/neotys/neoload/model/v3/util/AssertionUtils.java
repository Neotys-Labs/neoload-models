package com.neotys.neoload.model.v3.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AssertionUtils {

	protected static final String PREFIX_NAME = "assertion_";

	private static final String EMPTY = "";

	private AssertionUtils() {
	}

	public static List<ContentAssertion> normalyze(final List<ContentAssertion> assertions) {
		// Retrieves all names
		final Set<String> names = getNames(assertions);

		// Constructs a new list of assertions with the filled name
		final AtomicInteger index = new AtomicInteger(1);
		final ImmutableList.Builder<ContentAssertion> copiedAssertions = new ImmutableList.Builder<>();
		for (final ContentAssertion assertion : assertions) {
			copiedAssertions.add(normalyze(assertion, names, index));
		}
		return copiedAssertions.build();
	}

	public static String normalyzeContains(final Optional<String> contains) {
		return contains.orElse(EMPTY);
	}

	private static ContentAssertion normalyze(final ContentAssertion assertion, final Set<String> names, final AtomicInteger index) {
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

	private static Set<String> getNames(final List<ContentAssertion> assertions) {
		final Set<String> names = Sets.newHashSet();
		assertions.forEach(assertion -> assertion.getName().ifPresent(names::add));
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
