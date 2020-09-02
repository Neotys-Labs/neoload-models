package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueContentAssertionPathValidator;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueContentAssertionPathValidator.class)
public @interface UniqueContentAssertionPathCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionPathCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
}
