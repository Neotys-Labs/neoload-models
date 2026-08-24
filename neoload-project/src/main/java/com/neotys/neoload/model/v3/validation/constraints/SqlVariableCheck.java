package com.neotys.neoload.model.v3.validation.constraints;

import com.neotys.neoload.model.v3.validation.constraintvalidators.SqlVariableValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = SqlVariableValidator.class)
public @interface SqlVariableCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.SqlVariableCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
