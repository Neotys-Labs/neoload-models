package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.UniqueElementNameCheck;

public final class UniqueElementNameValidator extends AbstractConstraintValidator<UniqueElementNameCheck, List<? extends Element>> {
	@Override
	public boolean isValid(final List<? extends Element> elements, final ConstraintValidatorContext context) {
		final Set<String> uniqueNames = new HashSet<>();
		int nullOrEmptyCount = 0;
		int total = 0;
		if ((elements != null) && (!elements.isEmpty())) {
			total = elements.size();
			for (final Element element : elements) {
				final String name = element.getName();
				if (!Strings.isNullOrEmpty(name)) {
					uniqueNames.add(name);
				}
				else {
					nullOrEmptyCount = nullOrEmptyCount + 1;
				}
			}
		}
		return ((uniqueNames.size() + nullOrEmptyCount) == total);
	}
}
