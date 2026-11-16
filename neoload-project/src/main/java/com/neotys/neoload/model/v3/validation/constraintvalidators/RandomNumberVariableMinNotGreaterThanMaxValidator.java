package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.RandomNumberVariable;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import javax.validation.ConstraintValidatorContext;

public final class RandomNumberVariableMinNotGreaterThanMaxValidator extends AbstractConstraintValidator<MinNotGreaterThanMaxCheck, RandomNumberVariable> {

	@Override
	public boolean isValid(final RandomNumberVariable variable, final ConstraintValidatorContext context) {
		return variable == null || variable.getMin() <= variable.getMax();
	}
}
