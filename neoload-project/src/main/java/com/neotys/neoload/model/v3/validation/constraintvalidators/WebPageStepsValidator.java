package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.validation.constraints.WebPageStepsCheck;
import java.util.List;
import javax.validation.ConstraintValidatorContext;

public final class WebPageStepsValidator extends AbstractConstraintValidator<WebPageStepsCheck, List<Step>> {
	@Override
	public boolean isValid(final List<Step> steps, final ConstraintValidatorContext context) {
		if (steps == null) {
			return true;
		}
		return steps.stream().allMatch(step -> step instanceof Request || step instanceof WebPage);
	}
}
