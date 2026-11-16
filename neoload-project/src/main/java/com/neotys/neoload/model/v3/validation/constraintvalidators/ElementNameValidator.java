package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.validation.constraints.ElementNameCheck;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

/**
 * Mirrors the forbidden-char set enforced by NLGui's
 * {@code com.neotys.nl.util.NameValidator#isLogicalNameValid}, plus a 100-char cap that
 * NameValidator does not enforce. Keep both lists in sync if either changes.
 */
public final class ElementNameValidator extends AbstractConstraintValidator<ElementNameCheck, String> {
	/** Forbidden chars, regex-escaped to be used inside a character class. */
	public static final String FORBIDDEN_CHARS = "£€$\"\\[\\]<>|*¤?§µ#`@^²°¨\\\\";
	private static final Pattern FORBIDDEN_CHAR = Pattern.compile("[" + FORBIDDEN_CHARS + "]");
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
		final Matcher forbidden = FORBIDDEN_CHAR.matcher(name);
		if (forbidden.find()) {
			report(context, "element name '" + name + "' contains forbidden character '" + forbidden.group() + "'.");
			return false;
		}
		return true;
	}

	private static void report(final ConstraintValidatorContext context, final String message) {
		if (context == null) {
			return;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
