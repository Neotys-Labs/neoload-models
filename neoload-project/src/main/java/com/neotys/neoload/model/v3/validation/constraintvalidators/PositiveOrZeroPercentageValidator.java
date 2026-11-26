package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.PositiveOrZeroPercentageCheck;

public final class PositiveOrZeroPercentageValidator extends AbstractMinPercentageValidator<PositiveOrZeroPercentageCheck> {
	public PositiveOrZeroPercentageValidator() {
		super(0);
	}
}
