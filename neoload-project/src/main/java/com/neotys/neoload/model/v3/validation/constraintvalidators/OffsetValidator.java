package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.Offset;
import com.neotys.neoload.model.v3.validation.constraints.OffsetCheck;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

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

		return isNonZeroOffset(object, context);
	}

	/**
	 * Whether the value is a single non-zero amount and unit, as parsed by {@link Offset#parse}. A rejected value is
	 * exposed to the message as {@code {rejectedValue}}, since the violation does not name a text value itself.
	 */
	static boolean isNonZeroOffset(final Object object, final ConstraintValidatorContext context) {
		final boolean valid = object instanceof String && Offset.parse((String) object).filter(offset -> offset.getAmount() != 0).isPresent();
		if (!valid) {
			context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("rejectedValue", object);
		}
		return valid;
	}
}
