package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Fork;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.While;
import com.neotys.neoload.model.v3.validation.constraints.SharedElementStepTypeCheck;
import java.util.List;
import java.util.TreeSet;
import javax.validation.ConstraintValidatorContext;

public final class SharedElementStepTypeValidator extends AbstractConstraintValidator<SharedElementStepTypeCheck, List<Step>> {

	@Override
	public boolean isValid(final List<Step> sharedElements, final ConstraintValidatorContext context) {
		final TreeSet<String> invalidStepType = new TreeSet<>();
		for (final Step sharedElement : sharedElements) {
			if (!(sharedElement instanceof Container) && !(sharedElement instanceof Loop)
					&& !(sharedElement instanceof While) && !(sharedElement instanceof Fork)) {
				invalidStepType.add(sharedElement.getName());
			}
		}
		if (invalidStepType.isEmpty()) {
			return true;
		}

		SharedElementSupport.fail(context, "must have a 'transaction', 'loop', 'while' or 'fork' step; invalid step type for: "
				+ String.join(", ", invalidStepType) + ".");
		return false;
	}
}
