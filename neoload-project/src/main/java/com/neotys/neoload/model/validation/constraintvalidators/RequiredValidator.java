package com.neotys.neoload.model.validation.constraintvalidators;

import java.util.Collection;
import java.util.Map;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.validation.constraints.RequiredCheck;

@Deprecated
public final class RequiredValidator extends AbstractConstraintValidator<RequiredCheck, Object> {
	@Override
	public boolean isValid(final Object object, final ConstraintValidatorContext context) {
		if (object == null) return false;
		
		if (object instanceof CharSequence) {
			return object.toString().trim().length() > 0;
		}
		
		if (object instanceof Collection<?>) {
			return !((Collection<?>) object).isEmpty();
		}
		
		if (object instanceof Map<?,?>) {
			return !((Map<?,?>) object).isEmpty();
		}
		
		return true;
	}
}
