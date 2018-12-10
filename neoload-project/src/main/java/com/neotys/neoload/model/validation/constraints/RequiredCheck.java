package com.neotys.neoload.model.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.neotys.neoload.model.validation.constraintvalidators.RequiredValidator;

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = RequiredValidator.class)
@ReportAsSingleViolation
@Deprecated
public @interface RequiredCheck {
	String message() default "{com.neotys.neoload.model.validation.constraints.RequiredCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
}
