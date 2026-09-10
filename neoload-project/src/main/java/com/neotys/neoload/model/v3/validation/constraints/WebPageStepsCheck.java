package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.WebPageStepsValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = WebPageStepsValidator.class)
public @interface WebPageStepsCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.WebPageStepsCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
