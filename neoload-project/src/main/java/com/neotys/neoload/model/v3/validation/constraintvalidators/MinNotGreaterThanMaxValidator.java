package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.MinMaxRange;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import java.math.BigDecimal;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;

public final class MinNotGreaterThanMaxValidator extends AbstractConstraintValidator<MinNotGreaterThanMaxCheck, MinMaxRange> {

	@Override
	public boolean isValid(final MinMaxRange range, final ConstraintValidatorContext context) {
		if (range == null) {
			return true;
		}

		final Optional<BigDecimal> min = range.comparableMin();
		final Optional<BigDecimal> max = range.comparableMax();
		return !min.isPresent() || !max.isPresent() || min.get().compareTo(max.get()) <= 0;
	}
}
