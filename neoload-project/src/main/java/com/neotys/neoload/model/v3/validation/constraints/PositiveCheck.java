package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.PositiveNumberValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.PositiveSecondsDurationValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = {PositiveNumberValidator.class, PositiveSecondsDurationValidator.class})
@ReportAsSingleViolation
public @interface PositiveCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.PositiveCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
	
	String unit() default "";
}
