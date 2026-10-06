package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueNameValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueNameValidator.class)
public @interface UniqueNameCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UniqueNameCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
