package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.neotys.neoload.model.v3.validation.constraintvalidators.RequiredContentAssertionValidator;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = RequiredContentAssertionValidator.class)
@ReportAsSingleViolation
public @interface RequiredContentAssertionCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.RequiredContentAssertionCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
}
