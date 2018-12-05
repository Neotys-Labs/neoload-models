package com.neotys.neoload.model.v3.validation.constraints;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueVariableExtractorPathValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueVariableExtractorPathValidator.class)
public @interface UniqueVariableExtractorPathCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UniqueVariableExtractorPathCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
}
