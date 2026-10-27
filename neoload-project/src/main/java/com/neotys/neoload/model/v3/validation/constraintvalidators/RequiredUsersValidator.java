package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.v3.validation.constraints.RequiredUsersCheck;
import javax.validation.ConstraintValidatorContext;

public final class RequiredUsersValidator extends AbstractConstraintValidator<RequiredUsersCheck, PeakLoadPolicy> {
	@Override
	public boolean isValid(final PeakLoadPolicy peak, final ConstraintValidatorContext context) {
		return peak == null || peak.getUsers().isPresent();
	}
}
