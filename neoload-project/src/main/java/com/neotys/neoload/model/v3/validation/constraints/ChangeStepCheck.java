package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.ChangeStepValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ChangeStepValidator.class)
@ReportAsSingleViolation
public @interface ChangeStepCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.ChangeStepCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
