package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.UniqueNameEntity;
import com.neotys.neoload.model.v3.validation.constraints.UniqueNameCheck;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class UniqueNameValidator extends AbstractUniqueKeyValidator<UniqueNameEntity>
		implements ConstraintValidator<UniqueNameCheck, List<? extends UniqueNameEntity>> {
	@Override
	public boolean isValid(final List<? extends UniqueNameEntity> items, final ConstraintValidatorContext context) {
		return hasUniqueKeys(items, context);
	}

	@Override
	String key(final UniqueNameEntity item) {
		return item.getName();
	}
}
