package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static com.neotys.neoload.model.v3.project.userpath.VariableModifier.Category.*;
import static com.neotys.neoload.model.v3.project.userpath.VariableModifier.Mode.*;

import com.neotys.neoload.model.v3.project.userpath.VariableModifier;
import com.neotys.neoload.model.v3.validation.constraints.VariableModifierCheck;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;

public final class VariableModifierValidator extends AbstractConstraintValidator<VariableModifierCheck, VariableModifier> {

	@Override
	public boolean isValid(final VariableModifier variableModifier, final ConstraintValidatorContext context) {
		if (variableModifier == null) {
			return true;
		}

		final VariableModifier.Category category = variableModifier.getCategory();
		final VariableModifier.Mode mode = variableModifier.getMode();
		final Optional<String> value = variableModifier.getValue();

		if (category == PREDEFINED && mode != NEXT_VALUE && mode != INIT_VALUE) {
			return false;
		}

		//value is not needed when category is "PREDEFINED"
		if (category == PREDEFINED ) {
			return value.isEmpty();
		}

		if (category == SHARED_QUEUE ) {
			if (mode != ADD_SHARED_QUEUE_VALUE && mode != POLL_SHARED_QUEUE) {
				return false;
			}
            return value.isPresent() && !value.get().trim().isEmpty();
		}

		throw new IllegalStateException("Unhandled category: " + category);
	}
}