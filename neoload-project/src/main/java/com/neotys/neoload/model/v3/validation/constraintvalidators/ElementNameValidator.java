package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.validation.constraints.ElementNameCheck;
import javax.validation.ConstraintValidatorContext;

/**
 * Mirrors the forbidden-char set enforced by NLGui's
 * {@code com.neotys.nl.util.NameValidator#isLogicalNameValid}, plus a 100-char cap that
 * NameValidator does not enforce. Keep both lists in sync if either changes.
 */
public final class ElementNameValidator extends AbstractConstraintValidator<ElementNameCheck, String> {
	private static final char[] FORBIDDEN_CHARS = { '£', '€', '$', '"', '[', ']', '<', '>', '|', '*', '¤', '?', '§',
			'µ', '#', '`', '@', '^', '²', '°', '¨', '\\' };
	private static final int MAX_NAME_LENGTH = 100;

	@Override
	public boolean isValid(final String name, final ConstraintValidatorContext context) {
		if (Strings.isNullOrEmpty(name)) {
			return true;
		}
		if (name.length() > MAX_NAME_LENGTH) {
			report(context, "element name '" + name + "' exceeds " + MAX_NAME_LENGTH + " characters.");
			return false;
		}
		for (final char c : name.toCharArray()) {
			if (isForbidden(c)) {
				report(context, "element name '" + name + "' contains forbidden character '" + c + "'.");
				return false;
			}
		}
		return true;
	}

	private static boolean isForbidden(final char c) {
		for (final char forbidden : FORBIDDEN_CHARS) {
			if (c == forbidden) {
				return true;
			}
		}
		return false;
	}

	private static void report(final ConstraintValidatorContext context, final String message) {
		if (context == null) {
			return;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
