package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.DatePatternCheck;

import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public final class DatePatternValidator extends AbstractConstraintValidator<DatePatternCheck, String> {

	// Special patterns handled by CurrentSimpleDateFormat that bypass SimpleDateFormat
	private static final String PATTERN_MILLIS = "milliseconds since the UNIX epoch";
	private static final String PATTERN_MILLIS_LEGACY = "currentTimeMillis";

	@Override
	public boolean isValid(final String pattern, final ConstraintValidatorContext context) {
		if (pattern == null || pattern.trim().isEmpty()) {
			return true;
		}
		if (PATTERN_MILLIS.equals(pattern) || PATTERN_MILLIS_LEGACY.equals(pattern)) {
			return true;
		}
		try {
			new SimpleDateFormat(pattern);
			return true;
		} catch (final IllegalArgumentException e) {
			return false;
		}
	}
}
