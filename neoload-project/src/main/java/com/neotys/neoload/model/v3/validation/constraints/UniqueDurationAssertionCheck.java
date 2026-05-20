package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueDurationAssertionValidator;

/**
 * Validates that an assertions list contains at most one {@code DurationAssertion}.
 * This mirrors the designer's {@code AssertionsConfiguration} which exposes a
 * single slot for the duration assertion ({@code setDurationAssertion}).
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueDurationAssertionValidator.class)
public @interface UniqueDurationAssertionCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UniqueDurationAssertionCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
