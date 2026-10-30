package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.constraints.MaxSecondsCheck;
import java.time.Duration;
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
			if (!(value instanceof Duration)) {
				return false;
			}
			final Duration time = (Duration) value;
			return !time.isZero() && !time.isNegative() && time.getNano() == 0 && time.getSeconds() <= MaxSecondsCheck.MAX_SECONDS;
		}
		else {
			if (!(value instanceof String)) {
				return false;
			}
			return (((String) value).trim().length() > 0);
		}		
	}
}
