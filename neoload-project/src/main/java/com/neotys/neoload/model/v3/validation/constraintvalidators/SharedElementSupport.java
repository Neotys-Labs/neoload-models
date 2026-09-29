package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.SharedElementRef;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidatorContext;

final class SharedElementSupport {

	private SharedElementSupport() {
	}

	static List<String> referencedNames(final Stream<Element> elements) {
		return elements.filter(SharedElementRef.class::isInstance)
				.map(Element::getName)
				.collect(Collectors.toList());
	}

	static void fail(final ConstraintValidatorContext context, final String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
