package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UserPathThinkTimeValidator;

/**
 * Validates a {@code UserPathThinkTime}: at least one of {@code override}, {@code factor} or {@code random}
 * must be set, and {@code override} cannot be combined with {@code factor}.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UserPathThinkTimeValidator.class)
public @interface UserPathThinkTimeCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UserPathThinkTimeCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
