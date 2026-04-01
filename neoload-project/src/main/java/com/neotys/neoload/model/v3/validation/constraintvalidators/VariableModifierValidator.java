package com.neotys.neoload.model.v3.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.userpath.VariableModifier;
import com.neotys.neoload.model.v3.validation.constraints.VariableModifierCheck;

public final class VariableModifierValidator extends AbstractConstraintValidator<VariableModifierCheck, VariableModifier> {

	@Override
	public boolean isValid(final VariableModifier variableModifier, final ConstraintValidatorContext context) {
		if (variableModifier == null) {
			return true;
		}

		final VariableModifier.Category category = variableModifier.getCategory();
		final VariableModifier.Mode mode = variableModifier.getMode();

		if (category == VariableModifier.Category.DEFINED) {
			if (!variableModifier.getVariableName().isPresent()) {
				return false;
			}
		}

		if (category == VariableModifier.Category.SHARED) {
			if (!variableModifier.getSharedVariableName().isPresent()) {
				return false;
			}
		}

		if (mode == VariableModifier.Mode.ADD_VALUE) {
			if (!variableModifier.getSrcVariableName().isPresent()) {
				return false;
			}
		}

		if (mode == VariableModifier.Mode.GET_VALUE) {
			if (!variableModifier.getDestVariableName().isPresent()) {
				return false;
			}
		}

		return true;
	}
}