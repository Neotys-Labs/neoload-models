package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Fork;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.While;
import com.neotys.neoload.model.v3.validation.constraints.SharedElementDefaultNameCheck;
import java.util.List;
import java.util.TreeSet;
import javax.validation.ConstraintValidatorContext;

public final class SharedElementDefaultNameValidator extends AbstractConstraintValidator<SharedElementDefaultNameCheck, List<Step>> {

	@Override
	public boolean isValid(final List<Step> sharedElements, final ConstraintValidatorContext context) {
		final TreeSet<String> defaultNamed = new TreeSet<>();
		for (final Step sharedElement : sharedElements) {
			if (usesDefaultName(sharedElement)) {
				defaultNamed.add(sharedElement.getName());
			}
		}
		if (defaultNamed.isEmpty()) {
			return true;
		}

		SharedElementSupport.fail(context, "must have an explicit name; entries left with their step's default name are not allowed: "
				+ String.join(", ", defaultNamed) + ".");
		return false;
	}

	private static boolean usesDefaultName(final Step sharedElement) {
		final String name = sharedElement.getName();
		if (sharedElement instanceof Container) {
			return Container.DEFAULT_NAME.equals(name);
		}
		if (sharedElement instanceof Loop) {
			return Loop.DEFAULT_NAME.equals(name);
		}
		if (sharedElement instanceof While) {
			return While.DEFAULT_NAME.equals(name);
		}
		if (sharedElement instanceof Fork) {
			return Fork.DEFAULT_NAME.equals(name);
		}
		return false;
	}
}
