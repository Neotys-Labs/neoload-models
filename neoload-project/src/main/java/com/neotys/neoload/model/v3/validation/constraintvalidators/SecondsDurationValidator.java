package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.binding.converter.DurationGrammar;
import com.neotys.neoload.model.v3.binding.converter.Durations;
import com.neotys.neoload.model.v3.validation.constraints.SecondsDurationCheck;
import javax.validation.ConstraintValidatorContext;

public final class SecondsDurationValidator extends AbstractConstraintValidator<SecondsDurationCheck, String> {

	@Override
	public boolean isValid(final String input, final ConstraintValidatorContext context) {
		return input == null || Durations.validate(input, DurationGrammar.SECONDS);
	}
}
