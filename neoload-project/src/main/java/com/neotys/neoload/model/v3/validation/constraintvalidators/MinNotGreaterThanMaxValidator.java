package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.MinMaxRange;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import javax.validation.ConstraintValidatorContext;

public final class MinNotGreaterThanMaxValidator extends AbstractConstraintValidator<MinNotGreaterThanMaxCheck, MinMaxRange> {

	@Override
	public boolean isValid(final MinMaxRange range, final ConstraintValidatorContext context) {
		return range == null || !range.minGreaterThanMax();
	}
}
