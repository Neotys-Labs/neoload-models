package com.neotys.neoload.model.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.scenario.StartAfter;
import com.neotys.neoload.model.validation.constraints.CompositeCheck;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
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
			if (!(value instanceof Integer)) {
				return false;
			}
			return ((Integer) value > 0);
		}
		else {
			if (!(value instanceof String)) {
				return false;
			}
			return (((String) value).trim().length() > 0);
		}		
	}
}
