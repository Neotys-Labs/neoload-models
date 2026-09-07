package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.ProjectNameCheck;
import java.util.Optional;
import java.util.function.Function;
import javax.validation.ConstraintValidatorContext;

/**
 * Mirrors NLGui's {@code com.neotys.nl.util.FileUtils#isValidNeoLoadProjectName} (identifier
 * chars only), plus a 100-char cap that FileUtils does not enforce.
 */
public final class ProjectNameValidator extends AbstractConstraintValidator<ProjectNameCheck, Optional<String>> {
	private static final int MAX_NAME_LENGTH = 100;

	@Override
	public boolean isValid(final Optional<String> name, final ConstraintValidatorContext context) {
		final Optional<String> presentName = Optional.ofNullable(name).flatMap(Function.identity());
		if (!presentName.isPresent()) {
			return true;
		}
		final String value = presentName.get();
		if (value.length() > MAX_NAME_LENGTH) {
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (!Character.isJavaIdentifierPart(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
