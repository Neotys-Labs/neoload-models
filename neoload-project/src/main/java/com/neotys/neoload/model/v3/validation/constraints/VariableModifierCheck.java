package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.VariableModifierValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Target({ TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { VariableModifierValidator.class })
@ReportAsSingleViolation
public @interface VariableModifierCheck {

	String message() default "{com.neotys.neoload.model.v3.validation.constraints.VariableModifierCheck.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}