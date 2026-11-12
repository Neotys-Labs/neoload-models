package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.PositiveDurationOrVariableValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.PositiveDurationValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.PositiveNumberValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { METHOD, FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = {PositiveNumberValidator.class, PositiveDurationValidator.class, PositiveDurationOrVariableValidator.class})
public @interface PositiveCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.PositiveCheck.message}";
	 
	Class<?>[] groups() default {};
	 
	Class<? extends Payload>[] payload() default {};
	
	String unit() default "";
}
