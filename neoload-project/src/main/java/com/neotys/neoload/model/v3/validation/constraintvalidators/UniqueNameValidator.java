package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.UniqueNameEntity;
import com.neotys.neoload.model.v3.validation.constraints.UniqueNameCheck;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintValidatorContext;

public final class UniqueNameValidator extends AbstractConstraintValidator<UniqueNameCheck, List<? extends UniqueNameEntity>> {
	@Override
	public boolean isValid(final List<? extends UniqueNameEntity> profiles, final ConstraintValidatorContext context) {
		if (profiles == null) {
			return true;
		}
		final Set<String> names = new HashSet<>();
		return profiles.stream()
				.map(UniqueNameEntity::getName)
				.filter(name -> !Strings.isNullOrEmpty(name))
				.allMatch(names::add);
	}
}
