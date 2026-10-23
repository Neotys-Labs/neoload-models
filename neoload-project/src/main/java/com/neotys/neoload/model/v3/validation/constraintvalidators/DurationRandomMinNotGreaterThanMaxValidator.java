package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import java.math.BigInteger;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

public final class DurationRandomMinNotGreaterThanMaxValidator extends AbstractConstraintValidator<MinNotGreaterThanMaxCheck, DurationRandom> {
	private static final Pattern MILLISECONDS = Pattern.compile("\\d+");

	@Override
	public boolean isValid(final DurationRandom duration, final ConstraintValidatorContext context) {
		if (duration == null) {
			return true;
		}
		final String min = duration.getMin().orElse("0");
		final String max = duration.getMax();
		if (!isMilliseconds(min) || !isMilliseconds(max)) {
			return true;
		}
		return new BigInteger(min).compareTo(new BigInteger(max)) <= 0;
	}

	private static boolean isMilliseconds(final String value) {
		return value != null && MILLISECONDS.matcher(value).matches();
	}
}
