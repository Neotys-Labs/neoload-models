package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.MaxSecondsValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * A second-based duration is stored as an int number of milliseconds by NeoLoad, which caps it at {@link #MAX_SECONDS}.
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = MaxSecondsValidator.class)
public @interface MaxSecondsCheck {
	long MAX_SECONDS = Integer.MAX_VALUE / 1000;

	String message() default "{com.neotys.neoload.model.v3.validation.constraints.MaxSecondsCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
