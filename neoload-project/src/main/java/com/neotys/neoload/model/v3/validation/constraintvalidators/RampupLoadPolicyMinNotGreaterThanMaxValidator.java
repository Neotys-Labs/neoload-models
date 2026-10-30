package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import javax.validation.ConstraintValidatorContext;

public final class RampupLoadPolicyMinNotGreaterThanMaxValidator extends AbstractConstraintValidator<MinNotGreaterThanMaxCheck, RampupLoadPolicy> {

	@Override
	public boolean isValid(final RampupLoadPolicy policy, final ConstraintValidatorContext context) {
		return policy == null || policy.getMaxUsers() == null || policy.getMinUsers().orElse(0) <= policy.getMaxUsers();
	}
}
