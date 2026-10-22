package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import javax.validation.ConstraintValidatorContext;

public final class StartAfterValidator extends AbstractConstraintValidator<CompositeCheck, StartAfter> {
	@Override
	public boolean isValid(final StartAfter startAfter, final ConstraintValidatorContext context) {
		if (startAfter == null) {
			return false;
		}
		final StartAfter.Type type = startAfter.getType();
		if (type == null) {
			return false;
		}
		final Object value = startAfter.getValue();
		if (value == null) {
			return false;
		}
		if (type == StartAfter.Type.TIME) {
			return value instanceof SecondsDuration && ((SecondsDuration) value).isLiteral() && ((SecondsDuration) value).getAmount() > 0;
		}
		return value instanceof String && !((String) value).trim().isEmpty();
	}
}
