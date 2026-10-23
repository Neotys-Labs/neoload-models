package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.DurationRandomMinNotGreaterThanMaxValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.RampupLoadPolicyMinNotGreaterThanMaxValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.RandomNumberVariableMinNotGreaterThanMaxValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.RandomStringVariableMinNotGreaterThanMaxValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validates that the min of an element's range is not greater than its max. Equal bounds are accepted, and a
 * bound that cannot be compared (a {@code ${variable}}) is not checked. Each supported element has its own
 * validator. {@code element}, {@code min} and {@code max} are the as-code names of the element and of its two
 * fields, used in the violation message.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {
		DurationRandomMinNotGreaterThanMaxValidator.class,
		RampupLoadPolicyMinNotGreaterThanMaxValidator.class,
		RandomNumberVariableMinNotGreaterThanMaxValidator.class,
		RandomStringVariableMinNotGreaterThanMaxValidator.class})
public @interface MinNotGreaterThanMaxCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String element();

	String min() default "min";

	String max() default "max";
}
