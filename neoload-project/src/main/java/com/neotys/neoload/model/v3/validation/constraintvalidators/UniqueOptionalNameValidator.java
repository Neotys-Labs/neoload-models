package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.UniqueOptionalNameEntity;
import com.neotys.neoload.model.v3.validation.constraints.UniqueNameCheck;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class UniqueOptionalNameValidator extends AbstractUniqueKeyValidator<UniqueOptionalNameEntity>
		implements ConstraintValidator<UniqueNameCheck, List<? extends UniqueOptionalNameEntity>> {
	@Override
	public boolean isValid(final List<? extends UniqueOptionalNameEntity> items, final ConstraintValidatorContext context) {
		return hasUniqueKeys(items, context);
	}

	@Override
	String key(final UniqueOptionalNameEntity item) {
		return item.getName().orElse(null);
	}
}
