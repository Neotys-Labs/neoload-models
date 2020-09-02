package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueContentAssertionNameValidator;

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueContentAssertionNameValidator.class)
public @interface UniqueContentAssertionNameCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
}
