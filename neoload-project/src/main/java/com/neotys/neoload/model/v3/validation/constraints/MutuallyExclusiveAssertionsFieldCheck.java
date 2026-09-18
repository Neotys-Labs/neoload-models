package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.AssertionsFieldValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = AssertionsFieldValidator.class)
public @interface MutuallyExclusiveAssertionsFieldCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.MutuallyExclusiveAssertionsFieldCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
