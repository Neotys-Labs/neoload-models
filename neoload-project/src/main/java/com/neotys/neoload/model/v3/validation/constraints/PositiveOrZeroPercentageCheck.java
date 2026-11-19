package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.PositiveOrZeroPercentageValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validates that a {@code PercentageOrVariable} is a variable or a non-negative percentage (at least 0).
 */
@Target({TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = PositiveOrZeroPercentageValidator.class)
public @interface PositiveOrZeroPercentageCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.PositiveOrZeroPercentageCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
