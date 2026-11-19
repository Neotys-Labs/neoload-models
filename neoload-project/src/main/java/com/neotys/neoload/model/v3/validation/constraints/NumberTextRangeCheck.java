package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.NumberTextRangeValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * A text made of digits must be a number between {@link #min()} and {@link #max()}; any other text, such as a
 * variable reference, is left to the other constraints of the field.
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = NumberTextRangeValidator.class)
public @interface NumberTextRangeCheck {
	long min();

	long max();

	String message() default "{com.neotys.neoload.model.v3.validation.constraints.NumberTextRangeCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
