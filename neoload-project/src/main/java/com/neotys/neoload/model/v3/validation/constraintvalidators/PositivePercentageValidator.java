package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.PositivePercentageCheck;

public final class PositivePercentageValidator extends AbstractMinPercentageValidator<PositivePercentageCheck> {
	public PositivePercentageValidator() {
		super(1);
	}
}
