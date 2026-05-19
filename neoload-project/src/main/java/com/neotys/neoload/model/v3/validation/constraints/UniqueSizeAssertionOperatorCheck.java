package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueSizeAssertionOperatorValidator;

/**
 * Validates that an assertions list contains at most one {@code SizeAssertion} per operator
 * ({@code equals}, {@code less_than}, {@code greater_than}). This mirrors the designer's
 * {@code AssertionsConfiguration} which exposes a single slot per operator
 * ({@code setSizeAssertionEqual}, {@code setSizeAssertionLesser}, {@code setSizeAssertionGreater}).
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueSizeAssertionOperatorValidator.class)
public @interface UniqueSizeAssertionOperatorCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UniqueSizeAssertionOperatorCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
