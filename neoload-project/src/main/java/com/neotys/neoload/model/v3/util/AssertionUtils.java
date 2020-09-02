package com.neotys.neoload.model.v3.util;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

public class AssertionUtils {

	protected static final String PREFIX_NAME = "assertion_";
	
	private static final String EMPTY = "";

	private AssertionUtils() {
	}

	public static List<ContentAssertion> normalyze(final List<ContentAssertion> assertions) {
		// Retrieves all names
		final Set<String> names = getNames(assertions);
		
		// Constructs a new list of assertions with the filled name
		int index = 1;
		final ImmutableList.Builder<ContentAssertion> copiedAssertions = new ImmutableList.Builder<>();
		for (ContentAssertion assertion : assertions) {
			final Optional<String> name = assertion.getName();
			if (name.isPresent()) {
				// If name exists, use the given assertion
				copiedAssertions.add(assertion);
			}
			else {				
				// If name doesn't exist, compute a name and do a copy from the given assertion with the computed name
				String computedName = "";
				do {
					computedName = computeName(index);
					index = index + 1;
				}
				while (contains(names, computedName));
				names.add(computedName);
				
				final ContentAssertion copiedAssertion = ContentAssertion.builder()
						.from(assertion)
						.name(computedName)
						.build();
				copiedAssertions.add(copiedAssertion);
			}
		}
		return copiedAssertions.build();
	}
	
	public static String normalyzeContains(final Optional<String> contains) {
		return contains.orElse(EMPTY);
	}
	
	private static Set<String> getNames(final List<ContentAssertion> assertions) {
		final Set<String> names = Sets.newHashSet();
		assertions.forEach(assertion -> assertion.getName().ifPresent(name -> names.add(name)));
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
