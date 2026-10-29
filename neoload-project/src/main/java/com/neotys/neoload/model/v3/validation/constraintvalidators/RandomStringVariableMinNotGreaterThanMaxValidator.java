package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.RandomStringVariable;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import javax.validation.ConstraintValidatorContext;

public final class RandomStringVariableMinNotGreaterThanMaxValidator extends AbstractConstraintValidator<MinNotGreaterThanMaxCheck, RandomStringVariable> {

	@Override
	public boolean isValid(final RandomStringVariable variable, final ConstraintValidatorContext context) {
		return variable == null || variable.getMinLength() <= variable.getMaxLength();
	}
}
