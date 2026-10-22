package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import javax.validation.ConstraintValidatorContext;

public final class StopAfterValidator extends AbstractConstraintValidator<CompositeCheck, StopAfter> {
	@Override
	public boolean isValid(final StopAfter stopAfter, final ConstraintValidatorContext context) {
		if (stopAfter == null) {
			return false;
		}
		final StopAfter.Type type = stopAfter.getType();
		if (type == null) {
			return false;
		}
		final Object value = stopAfter.getValue();
		if (type != StopAfter.Type.TIME) {
			return value == null;
		}
		if (InvalidDuration.isInvalid(value)) {
			return InvalidDuration.report(context, value);
		}
		return value instanceof SecondsDuration && ((SecondsDuration) value).isLiteral() && ((SecondsDuration) value).getAmount() > 0;
	}
}
