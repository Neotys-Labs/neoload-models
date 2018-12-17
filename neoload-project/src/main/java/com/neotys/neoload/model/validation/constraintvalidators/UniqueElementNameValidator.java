package com.neotys.neoload.model.validation.constraintvalidators;

import com.google.common.base.Strings;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.validation.constraints.UniqueElementNameCheck;

import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
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
