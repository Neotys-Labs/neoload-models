package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.DateVariable;
import com.neotys.neoload.model.v3.validation.constraints.StartDateMatchesPatternCheck;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.validation.ConstraintValidatorContext;

public final class StartDateMatchesPatternValidator extends AbstractConstraintValidator<StartDateMatchesPatternCheck, DateVariable> {

	// Both special pattern values (checked by DatePatternCheck) have no real date/time format to
	// match a start_date against.
	private static final String PATTERN_MILLIS = "milliseconds since the UNIX epoch";
	private static final String PATTERN_MILLIS_LEGACY = "currentTimeMillis";

	@Override
	public boolean isValid(final DateVariable variable, final ConstraintValidatorContext context) {
		final String pattern = variable.getPattern();
		if (PATTERN_MILLIS.equals(pattern) || PATTERN_MILLIS_LEGACY.equals(pattern)) {
			return true;
		}
		try {
			final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			dateFormat.setLenient(false);
			dateFormat.parse(variable.getStartDate());
			return true;
		} catch (final ParseException | IllegalArgumentException e) {
			return false;
		}
	}
}
