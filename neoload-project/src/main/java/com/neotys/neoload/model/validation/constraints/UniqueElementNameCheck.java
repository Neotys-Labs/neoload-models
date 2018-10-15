package com.neotys.neoload.model.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.neotys.neoload.model.validation.constraintvalidators.UniqueElementNameValidator;

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueElementNameValidator.class)
public @interface UniqueElementNameCheck {
	String message() default "{com.neotys.neoload.model.validation.constraints.UniqueElementNameCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
}
