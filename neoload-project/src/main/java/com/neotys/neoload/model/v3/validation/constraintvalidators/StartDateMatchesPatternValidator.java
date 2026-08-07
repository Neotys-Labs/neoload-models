package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.DatePatternElement;
import com.neotys.neoload.model.v3.project.variable.DateVariable;
import com.neotys.neoload.model.v3.validation.constraints.StartDateMatchesPatternCheck;

import javax.validation.ConstraintValidatorContext;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public final class StartDateMatchesPatternValidator extends AbstractConstraintValidator<StartDateMatchesPatternCheck, DateVariable> {

	private static final String PATTERN_MILLIS = "milliseconds since the UNIX epoch";
	private static final String PATTERN_MILLIS_LEGACY = "currentTimeMillis";

	@Override
	public boolean isValid(final DateVariable dateVariable, final ConstraintValidatorContext context) {
		final String startDate = dateVariable.getStartDate();
		if (startDate == null || startDate.trim().isEmpty()) {
			// @RequiredCheck handles the null/empty case
			return true;
		}
		String pattern = dateVariable.getPattern();
		if (pattern == null || pattern.trim().isEmpty()) {
			pattern = DatePatternElement.DEFAULT_PATTERN;
		}
		return isValidDate(startDate, pattern);
	}

	private boolean isValidDate(final String date, final String pattern) {
		if (PATTERN_MILLIS.equals(pattern) || PATTERN_MILLIS_LEGACY.equals(pattern)) {
			try {
				Long.parseLong(date);
				return true;
			} catch (final NumberFormatException e) {
				return false;
			}
		}
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setLenient(false);
			final ParsePosition pos = new ParsePosition(0);
			return sdf.parse(date, pos) != null && pos.getIndex() == date.length();
		} catch (final IllegalArgumentException e) {
			// Invalid pattern — @DatePatternCheck is responsible for this case
			return true;
		}
	}
}
