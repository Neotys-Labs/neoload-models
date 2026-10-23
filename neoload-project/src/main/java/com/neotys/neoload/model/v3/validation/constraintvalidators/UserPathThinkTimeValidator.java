package com.neotys.neoload.model.v3.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.UserPathThinkTime;
import com.neotys.neoload.model.v3.validation.constraints.UserPathThinkTimeCheck;

public final class UserPathThinkTimeValidator extends AbstractConstraintValidator<UserPathThinkTimeCheck, UserPathThinkTime> {

	private static final String OVERRIDE_WITH_FACTOR_MESSAGE =
			"{com.neotys.neoload.model.v3.validation.constraints.UserPathThinkTimeCheck.overrideWithFactor.message}";

	@Override
	public boolean isValid(final UserPathThinkTime thinkTime, final ConstraintValidatorContext context) {
		if (thinkTime == null) {
			return true;
		}

		final boolean hasOverride = thinkTime.getOverride().isPresent();
		final boolean hasFactor = thinkTime.getFactor().isPresent();

		if (hasOverride && hasFactor) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(OVERRIDE_WITH_FACTOR_MESSAGE).addConstraintViolation();
			return false;
		}

		return hasOverride || hasFactor || thinkTime.getRandom().isPresent();
	}
}
