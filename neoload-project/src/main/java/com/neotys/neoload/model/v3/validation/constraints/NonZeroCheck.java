package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.NonZeroDurationValidator;
import com.neotys.neoload.model.v3.validation.constraintvalidators.NonZeroValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = {NonZeroValidator.class, NonZeroDurationValidator.class})
public @interface NonZeroCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.NonZeroCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
