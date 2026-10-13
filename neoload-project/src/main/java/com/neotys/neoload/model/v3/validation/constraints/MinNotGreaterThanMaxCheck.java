package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.MinNotGreaterThanMaxValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validates a {@link com.neotys.neoload.model.v3.project.MinMaxRange}: its min must not be greater than
 * its max. Equal bounds are accepted. {@code element}, {@code min} and {@code max} are the as-code names
 * of the element and of its two fields, used in the violation message.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MinNotGreaterThanMaxValidator.class)
public @interface MinNotGreaterThanMaxCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String element();

	String min() default "min";

	String max() default "max";
}
