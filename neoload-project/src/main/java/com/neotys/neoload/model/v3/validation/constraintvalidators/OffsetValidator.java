package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.Offset;
import com.neotys.neoload.model.v3.validation.constraints.OffsetCheck;
import javax.validation.ConstraintValidatorContext;

public final class OffsetValidator extends AbstractConstraintValidator<OffsetCheck, Offset> {
	@Override
	public boolean isValid(final Offset offset, final ConstraintValidatorContext context) {
		return offset == null || !offset.isInvalid();
	}
}
