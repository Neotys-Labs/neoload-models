package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import javax.validation.ConstraintValidatorContext;

/**
 * Reports a duration that follows no grammar, naming its raw text, from a validator whose own message
 * does not carry the validated value.
 */
final class InvalidDuration {
	private static final String MESSAGE = "'%s' is not a valid duration, expected ordered units such as 1h30m15s or a number of seconds.";

	private InvalidDuration() {
		throw new IllegalStateException("Utility class");
	}

	static boolean isInvalid(final Object value) {
		return value instanceof SecondsDuration && ((SecondsDuration) value).isInvalid();
	}

	static boolean report(final ConstraintValidatorContext context, final Object invalidValue) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(escape(String.format(MESSAGE, invalidValue))).addConstraintViolation();
		return false;
	}

	private static String escape(final String text) {
		return text.replaceAll("([\\\\{}$])", "\\\\$1");
	}
}
