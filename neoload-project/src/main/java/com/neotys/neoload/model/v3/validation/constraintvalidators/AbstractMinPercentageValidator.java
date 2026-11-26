package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.PercentageLiteral;
import com.neotys.neoload.model.v3.project.userpath.PercentageOrVariable;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that a {@link PercentageOrVariable} is a variable or a percentage of at least a minimum.
 */
abstract class AbstractMinPercentageValidator<A extends Annotation> extends AbstractConstraintValidator<A, PercentageOrVariable> {
	private final int minimum;

	AbstractMinPercentageValidator(final int minimum) {
		this.minimum = minimum;
	}

	@Override
	public final boolean isValid(final PercentageOrVariable percentage, final ConstraintValidatorContext context) {
		return !(percentage instanceof PercentageLiteral) || ((PercentageLiteral) percentage).getValue() >= minimum;
	}
}
