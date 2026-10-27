package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.QTestIdsCheck;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

public final class QTestIdsValidator extends AbstractConstraintValidator<QTestIdsCheck, Map<String, String>> {
	private static final List<String> ID_KEYS = List.of("qtest.project_id", "qtest.cycle_id");
	private static final Pattern POSITIVE_INTEGER = Pattern.compile("[1-9]\\d{0,18}");

	@Override
	public boolean isValid(final Map<String, String> settings, final ConstraintValidatorContext context) {
		return settings == null || ID_KEYS.stream()
				.filter(settings::containsKey)
				.map(settings::get)
				.allMatch(value -> value != null && POSITIVE_INTEGER.matcher(value.trim()).matches());
	}
}
