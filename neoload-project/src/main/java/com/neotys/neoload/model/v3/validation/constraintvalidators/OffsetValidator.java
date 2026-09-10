package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.CurrentDateVariable.Offset;
import com.neotys.neoload.model.v3.validation.constraints.OffsetCheck;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;

public final class OffsetValidator extends AbstractConstraintValidator<OffsetCheck, Object> {

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

		return object instanceof String && Offset.parse((String) object).isPresent();
	}
}
