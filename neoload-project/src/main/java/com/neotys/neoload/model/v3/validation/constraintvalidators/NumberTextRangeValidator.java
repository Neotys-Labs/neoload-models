package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.NumberTextRangeCheck;
import java.math.BigInteger;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

public final class NumberTextRangeValidator extends AbstractConstraintValidator<NumberTextRangeCheck, String> {
	private static final Pattern DIGITS = Pattern.compile("\\d+");

	private BigInteger min;
	private BigInteger max;

	@Override
	public void initialize(final NumberTextRangeCheck constraintAnnotation) {
		this.min = BigInteger.valueOf(constraintAnnotation.min());
		this.max = BigInteger.valueOf(constraintAnnotation.max());
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (value == null || !DIGITS.matcher(value).matches()) {
			return true;
		}
		final BigInteger number = new BigInteger(value);
		return number.compareTo(min) >= 0 && number.compareTo(max) <= 0;
	}
}
