package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.NonNegativePercentageCheck;

public final class NonNegativePercentageValidator extends AbstractMinPercentageValidator<NonNegativePercentageCheck> {
	public NonNegativePercentageValidator() {
		super(0);
	}
}
