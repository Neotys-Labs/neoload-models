package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.QTestIdsCheck;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidatorContext;

public final class QTestIdsValidator extends AbstractConstraintValidator<QTestIdsCheck, Map<String, String>> {
	private static final List<String> ID_KEYS = List.of("qtest.project_id", "qtest.cycle_id");
	private static final Pattern POSITIVE_INTEGER = Pattern.compile("[1-9]\\d{0,18}");

	@Override
	public boolean isValid(final Map<String, String> settings, final ConstraintValidatorContext context) {
		if (settings == null) {
			return true;
		}
		final List<String> invalidKeys = ID_KEYS.stream()
				.filter(settings::containsKey)
				.filter(key -> !isPositiveInteger(settings.get(key)))
				.collect(Collectors.toList());
		if (invalidKeys.isEmpty()) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		invalidKeys.forEach(key -> context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
				.addPropertyNode(key)
				.addConstraintViolation());
		return false;
	}

	private static boolean isPositiveInteger(final String value) {
		return value != null && POSITIVE_INTEGER.matcher(value.trim()).matches();
	}
}
