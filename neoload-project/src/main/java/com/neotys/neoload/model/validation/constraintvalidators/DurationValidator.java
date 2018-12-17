package com.neotys.neoload.model.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.validation.constraints.CompositeCheck;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public final class DurationValidator extends AbstractConstraintValidator<CompositeCheck, Duration> {
	@Override
	public boolean isValid(final Duration duration, final ConstraintValidatorContext context) {
		
		if (duration == null) return false;
		
		final Duration.Type type = duration.getType();
		if (type == null) return false;

		final Integer value = duration.getValue();
		if (value == null) return false;
		return value > 0;
	}
}
