package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.IncrementTimeUnit;
import com.neotys.neoload.model.v3.validation.constraints.ChangeStepCheck;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;

public final class ChangeStepValidator extends AbstractConstraintValidator<ChangeStepCheck, Object> {

	@Override
	public boolean isValid(final Object input, final ConstraintValidatorContext context) {
		// optional values are valid
		Object object = input;
		if (object instanceof Optional) {
			object = ((Optional<?>) object).orElse(null);
		}

		// null values are valid
		if (object == null) {
			return true;
		}

		return object instanceof String && IncrementTimeUnit.PATTERN.matcher((String) object).matches();
	}
}
