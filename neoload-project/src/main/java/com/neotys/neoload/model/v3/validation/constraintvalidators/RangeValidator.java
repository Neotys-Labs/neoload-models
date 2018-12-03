package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;

public final class RangeValidator extends AbstractConstraintValidator<RangeCheck, Object> {
	private BigDecimal minValue;
	private BigDecimal maxValue;

	@Override
	public void initialize(RangeCheck constraintAnnotation) {
		this.minValue = new BigDecimal(constraintAnnotation.min());
		this.maxValue = new BigDecimal(constraintAnnotation.max());
	}

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

		if (object instanceof Number) {
			final Number number = (Number) object;
			try {
				int minResult = new BigDecimal(number.toString() ).compareTo(minValue);
				int maxResult = new BigDecimal(number.toString() ).compareTo(maxValue);
				return minResult >= 0 && maxResult <= 0;
			}
			catch (NumberFormatException nfe) {
				return false;
			}
		}
		return false;
	}
}
