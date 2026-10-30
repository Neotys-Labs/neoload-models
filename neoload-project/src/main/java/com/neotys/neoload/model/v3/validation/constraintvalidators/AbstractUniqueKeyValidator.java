package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.google.common.base.Strings;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

/**
 * Checks that the keys of a list are unique. Null items and null or empty keys are ignored, keys are
 * compared case-sensitively. The distinct duplicated keys, in order of first appearance, are exposed to the
 * message as the {@code duplicates} parameter.
 * <p>
 * Does not implement {@code ConstraintValidator} itself: Hibernate Validator resolves the validated type from the
 * first parameterization it meets, so each subclass declares its own.
 */
abstract class AbstractUniqueKeyValidator<E> {
	private static final String DUPLICATES = "duplicates";

	abstract String key(E item);

	final boolean hasUniqueKeys(final List<? extends E> items, final ConstraintValidatorContext context) {
		if (items == null) {
			return true;
		}
		final Set<String> keys = new HashSet<>();
		final Set<String> duplicates = new LinkedHashSet<>();
		items.stream()
				.filter(Objects::nonNull)
				.map(this::key)
				.filter(key -> !Strings.isNullOrEmpty(key))
				.filter(key -> !keys.add(key))
				.forEach(duplicates::add);
		if (duplicates.isEmpty()) {
			return true;
		}
		context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter(DUPLICATES, String.join(", ", duplicates));
		return false;
	}
}
