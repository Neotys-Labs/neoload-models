package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.userpath.Case;
import com.neotys.neoload.model.v3.validation.constraints.UniqueCaseValueCheck;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintValidatorContext;

public final class UniqueCaseValueValidator extends AbstractConstraintValidator<UniqueCaseValueCheck, List<Case>> {
	@Override
	public boolean isValid(final List<Case> cases, final ConstraintValidatorContext context) {
		if (cases == null) {
			return true;
		}
		final Set<String> values = new HashSet<>();
		return cases.stream()
				.map(Case::getValue)
				.filter(value -> !Strings.isNullOrEmpty(value))
				.allMatch(values::add);
	}
}
