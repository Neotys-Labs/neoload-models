package com.neotys.neoload.model.v3.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.neotys.neoload.model.v3.validation.constraintvalidators.UniqueWsKeystorePathValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueWsKeystorePathValidator.class)
public @interface UniqueWsKeystorePathCheck {
	String message() default "{com.neotys.neoload.model.v3.validation.constraints.UniqueWsKeystorePathCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
