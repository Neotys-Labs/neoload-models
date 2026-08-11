package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.CaughtExceptionsValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CaughtExceptionsValidator.class)
public @interface CaughtExceptionsCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.CaughtExceptionsCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}