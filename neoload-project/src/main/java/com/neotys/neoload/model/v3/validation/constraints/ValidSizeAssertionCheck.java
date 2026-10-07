package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.neotys.neoload.model.v3.validation.constraintvalidators.ValidSizeAssertionValidator;

/**
 * Validates a {@code SizeAssertion}: at least one bound must be set, and the exact size
 * ({@code equals}) cannot be combined with {@code greater_than} or {@code less_than}.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidSizeAssertionValidator.class)
@ReportAsSingleViolation
public @interface ValidSizeAssertionCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.ValidSizeAssertionCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
